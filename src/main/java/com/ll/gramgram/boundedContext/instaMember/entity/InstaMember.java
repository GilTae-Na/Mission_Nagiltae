package com.ll.gramgram.boundedContext.instaMember.entity;

import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)//상위 클래스 까지 출력? 위해
@Entity
@Getter
public class InstaMember extends InstaMemberBase{
    @Column(unique = true)
    private String username;

    @OneToMany(mappedBy = "fromInstaMember", cascade = {CascadeType.ALL})
    @OrderBy("id desc") // 정렬
    @LazyCollection(LazyCollectionOption.EXTRA)
    @Builder.Default // @Builder 가 있으면 ` = new ArrayList<>();` 가 작동하지 않는다. 그래서 이걸 붙여야 한다.
    private List<LikeablePerson> fromLikeablePeople = new ArrayList<>();

    @OneToMany(mappedBy = "toInstaMember", cascade = {CascadeType.ALL})
    @OrderBy("id desc") // 정렬
    @Builder.Default // @Builder 가 있으면 ` = new ArrayList<>();` 가 작동하지 않는다. 그래서 이걸 붙여야 한다.
    private List<LikeablePerson> toLikeablePeople = new ArrayList<>();

    public void addFromLikeablePerson(LikeablePerson likeablePerson) {
        fromLikeablePeople.add(0, likeablePerson);
    }

    public void addToLikeablePerson(LikeablePerson likeablePerson) {
        toLikeablePeople.add(0, likeablePerson);
    }

    public void removeFromLikeablePerson(LikeablePerson likeablePerson) {
        fromLikeablePeople.removeIf(e -> e.equals(likeablePerson));
    }

    public void removeToLikeablePerson(LikeablePerson likeablePerson) {
        toLikeablePeople.removeIf(e -> e.equals(likeablePerson));
    }

    public String getGenderDisplayName() {
        return switch (gender) {
            case "W" -> "여성";
            default -> "남성";
        };
    }

    public String getGenderDisplayNameWithIcon() {
        return switch (gender) {
            case "W" -> "<i class=\"fa-solid fa-person-dress\"></i>";
            default -> "<i class=\"fa-solid fa-person\"></i>";
        } + "&nbsp;" + getGenderDisplayName();
    }

    public void increaseLikesCount(String gender, int attractiveTypeCode) {
        if (gender.equals("W") && attractiveTypeCode == 1) likesCountByGenderWomanAndAttractiveTypeCode1++;
        if (gender.equals("W") && attractiveTypeCode == 2) likesCountByGenderWomanAndAttractiveTypeCode2++;
        if (gender.equals("W") && attractiveTypeCode == 3) likesCountByGenderWomanAndAttractiveTypeCode3++;
        if (gender.equals("M") && attractiveTypeCode == 1) likesCountByGenderManAndAttractiveTypeCode1++;
        if (gender.equals("M") && attractiveTypeCode == 2) likesCountByGenderManAndAttractiveTypeCode2++;
        if (gender.equals("M") && attractiveTypeCode == 3) likesCountByGenderManAndAttractiveTypeCode3++;
    }

    public void decreaseLikesCount(String gender, int attractiveTypeCode) {
        if (gender.equals("W") && attractiveTypeCode == 1) likesCountByGenderWomanAndAttractiveTypeCode1--;
        if (gender.equals("W") && attractiveTypeCode == 2) likesCountByGenderWomanAndAttractiveTypeCode2--;
        if (gender.equals("W") && attractiveTypeCode == 3) likesCountByGenderWomanAndAttractiveTypeCode3--;
        if (gender.equals("M") && attractiveTypeCode == 1) likesCountByGenderManAndAttractiveTypeCode1--;
        if (gender.equals("M") && attractiveTypeCode == 2) likesCountByGenderManAndAttractiveTypeCode2--;
        if (gender.equals("M") && attractiveTypeCode == 3) likesCountByGenderManAndAttractiveTypeCode3--;
    }

    public void updateGender(String gender) {
        this.gender = gender;
    }

    public InstaMemberSnapshot snapshot(String eventTypeCode) {
        return InstaMemberSnapshot
                .builder()
                .eventTypeCode(eventTypeCode)
                .username(username)
                .instaMember(this)
                .gender(gender)
                .likesCountByGenderManAndAttractiveTypeCode1(likesCountByGenderManAndAttractiveTypeCode1)
                .likesCountByGenderManAndAttractiveTypeCode2(likesCountByGenderManAndAttractiveTypeCode2)
                .likesCountByGenderManAndAttractiveTypeCode3(likesCountByGenderManAndAttractiveTypeCode3)
                .likesCountByGenderWomanAndAttractiveTypeCode1(likesCountByGenderWomanAndAttractiveTypeCode1)
                .likesCountByGenderWomanAndAttractiveTypeCode2(likesCountByGenderWomanAndAttractiveTypeCode2)
                .likesCountByGenderWomanAndAttractiveTypeCode3(likesCountByGenderWomanAndAttractiveTypeCode3)
                .build();

    }
}
