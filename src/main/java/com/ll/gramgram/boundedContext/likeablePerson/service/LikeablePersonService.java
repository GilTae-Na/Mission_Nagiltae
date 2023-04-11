package com.ll.gramgram.boundedContext.likeablePerson.service;

import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.instaMember.service.InstaMemberService;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.likeablePerson.repository.LikeablePersonRepository;
import com.ll.gramgram.boundedContext.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeablePersonService {
    private final LikeablePersonRepository likeablePersonRepository;
    private final InstaMemberService instaMemberService;

    @Transactional
    public RsData<LikeablePerson> like(Member member, String username, int attractiveTypeCode) {

        List<LikeablePerson> fromLikeablePeople = member.getInstaMember().getFromLikeablePeople();
        //현제 유저가 좋아하는 사람 리스트

        if (member.hasConnectedInstaMember() == false) {
            return RsData.of("F-1", "먼저 본인의 인스타그램 아이디를 입력해야 합니다.");
        }

        if (member.getInstaMember().getUsername().equals(username)) {
            return RsData.of("F-2", "본인을 호감상대로 등록할 수 없습니다.");
        }

        if(fromLikeablePeople.size()>=10){ //case 5
            return RsData.of("F-3", "호감상대는 10명을 넘어갈 수 없습니다.");
        }

        for(LikeablePerson likeablePerson : fromLikeablePeople){
            if(duplicateUsername(likeablePerson, username, attractiveTypeCode)){ //case 4
                // 입력한 이름이 내가 좋아하는 사람 목록에 이미 있고, 호감코드까지 같을때
                return RsData.of("F-4", "중복으로 호감표시를 할 수 없습니다.");
            }
            else if(updateAttractCode(likeablePerson, username, attractiveTypeCode)){ //case 6
                String preAttractCode = likeablePerson.getAttractiveTypeDisplayName(); //이전 매력코드
                likeablePerson.setAttractiveTypeCode(attractiveTypeCode); //매력코드 업데이트
                String postAttractCode = likeablePerson.getAttractiveTypeDisplayName(); //바뀐 매력코드
                return RsData.of("S-2", String.format("%s 에 대한 호감사유를 %s에서 %s로 변경합니다.", username, preAttractCode, postAttractCode));
            }
        }


        InstaMember fromInstaMember = member.getInstaMember();
        InstaMember toInstaMember = instaMemberService.findByUsernameOrCreate(username).getData();

        LikeablePerson likeablePerson = LikeablePerson
                .builder()
                .fromInstaMember(fromInstaMember) // 호감을 표시하는 사람의 인스타 멤버
                .fromInstaMemberUsername(member.getInstaMember().getUsername()) // 중요하지 않음
                .toInstaMember(toInstaMember) // 호감을 받는 사람의 인스타 멤버
                .toInstaMemberUsername(toInstaMember.getUsername()) // 중요하지 않음
                .attractiveTypeCode(attractiveTypeCode) // 1=외모, 2=능력, 3=성격
                .build();

        likeablePersonRepository.save(likeablePerson); // 저장

        // 너가 좋아하는 호감표시 생겼어.
        fromInstaMember.addFromLikeablePerson(likeablePerson);

        // 너를 좋아하는 호감표시 생겼어.
        toInstaMember.addToLikeablePerson(likeablePerson);

        return RsData.of("S-1", "입력하신 인스타유저(%s)를 호감상대로 등록되었습니다.".formatted(username), likeablePerson);
    }

    public boolean duplicateUsername(LikeablePerson likeablePerson, String username, int attractiveTypeCode) {
        if (likeablePerson.getToInstaMemberUsername().equals(username) && likeablePerson.getAttractiveTypeCode() == attractiveTypeCode) {
            return true;
        }
        return false;
    }

    public boolean updateAttractCode(LikeablePerson likeablePerson, String username, int attractiveTypeCode){
        if(likeablePerson.getToInstaMemberUsername().equals(username) && likeablePerson.getAttractiveTypeCode() != attractiveTypeCode){
            return true;
        }
        return false;
    }

    public List<LikeablePerson> findByFromInstaMemberId(Long fromInstaMemberId) {
        return likeablePersonRepository.findByFromInstaMemberId(fromInstaMemberId);
    }

    public Optional<LikeablePerson> findById(Long id) {
        return likeablePersonRepository.findById(id);
    }

    @Transactional
    public RsData delete(LikeablePerson likeablePerson) {
        likeablePersonRepository.delete(likeablePerson);

        String likeCanceledUsername = likeablePerson.getToInstaMember().getUsername();
        return RsData.of("S-1", "%s님에 대한 호감을 취소하였습니다.".formatted(likeCanceledUsername));
    }

    public RsData canActorDelete(Member actor, LikeablePerson likeablePerson) {
        if (likeablePerson == null) return RsData.of("F-1", "이미 삭제되었습니다.");

        // 수행자의 인스타계정 번호
        long actorInstaMemberId = actor.getInstaMember().getId();
        // 삭제 대상의 작성자(호감표시한 사람)의 인스타계정 번호
        long fromInstaMemberId = likeablePerson.getFromInstaMember().getId();

        if (actorInstaMemberId != fromInstaMemberId)
            return RsData.of("F-2", "권한이 없습니다.");

        return RsData.of("S-1", "삭제가능합니다.");
    }




}
