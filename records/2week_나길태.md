# 1Week 나길태
-----------------------------------------------------------------
### 미션 요구사항 분석 & 체크리스트
-----------------------------------------------------------------
#### 요구사항분석
1. 한명의 인스타회원이 다른 인스타회원에게 중복으로 호감표시를 할 수 없습니다.
- 예를들어 본인의 인스타ID가 aaaa, 상대의 인스타ID가 bbbb 라고 하자.
- aaaa 는 bbbb 에게 호감을 표시한다.(사유 : 외모)
- 잠시 후 aaaa 는 bbbb 에게 다시 호감을 표시한다.(사유 : 외모)
- 이 경우에는 처리되면 안된다.(rq.historyBack)

2. 한명의 인스타회원이 11명 이상의 호감상대를 등록 할 수 없습니다.
- 예를들어 본인의 인스타ID가 aaaa 라고 하자.
- aaaa 는 bbbb, cccc, dddd, eeee, ffff, gggg, hhhh, iiii, jjjj, kkkk 에 대해서 호감표시를 했다.(사유는 상관없음, aaaa는 현재까지 10명에게 호감표시를 했음)
- 잠시 후 aaaa 는 llll 에게 호감표시를 한다.(사유는 상관없음)
- 이 경우에는 처리되면 안된다.(rq.historyBack)

3. 케이스 4 가 발생했을 때 기존의 사유와 다른 사유로 호감을 표시하는 경우에는 성공으로 처리한다.
- 예를들어 본인의 인스타ID가 aaaa, 상대의 인스타ID가 bbbb 라고 하자.
- aaaa 는 bbbb 에게 호감을 표시한다.(사유 : 외모)
- 잠시 후 aaaa 는 bbbb 에게 다시 호감을 표시한다.(사유 : 성격)
- 이 경우에는 새 호감상대로 등록되지 않고, 기존 호감표시에서 사유만 수정된다.
 - 외모 => 성격
 - resultCode=S-2
 - msg=bbbb 에 대한 호감사유를 외모에서 성격으로 변경합니다.


#### 체크리스트
-  [√] 테스트 케이스 만들기
-  [√] 데이터 양방향 매핑 파악
-  [√] RsData를 통한 메세지 전송 -> rq.historyBack으로 구현가능
-  [√] 테스트 케이스 만들기


---------------------------------------------------------------------------
#### N주차 미션 요약
[접근 방법]
InstaMember과 LikablePerson에 있는 양방향 관계에서 어떤 방식으로 데이터를 꺼내올지 고민을 했다
1:N 관계로 실제 db에 저장되는건 ID간의 관계가 저장되었다. (from_insta_member_id, to_insta_member_id)
이걸 이해하고 바로toToInstaMemberUsername으로 접근해서 값을 가져올수 있었다.

case:4 
- rq.member로 InstaMember를 가져올 수 있고 여기서 또 getFromLikeablePeople()로 유저가 좋아하는 호감목록을 가져올 수 있다.
- LikablePerson 리스트 형태인데 여기서 for each 문으로 각각 getToInstaMemberUsername()를 입력된 Username과 비교해 등록된 인스타아이디인지 확인한다.
- 마찬가지로 AttractCode도 비교한다.
- 둘다 겹칠경우 RsData로 F-4 실패코드를 날린다.
case:5 
- member.getInstaMember().getFromLikeablePeople() = 내가 좋아하는 사람들 목록, size()로 크기를 구할 수있다.
case:6
- case:4 수행중 이름은 겹치는데 AttractCode가 겹치지 않을경우 
- 이전 AttractCode와 바뀐 AttractCode를 저장해 String.format으로 출력했다.
- 바뀐 attractCode저장은 entity에 setter를 넣어주었다.

[특이사항]



참고: [Refactoring]
Refactoring 시 주로 다루어야 할 이슈들에 대해 리스팅합니다.
1차 리팩토링은 기능 개발을 종료한 후, 스스로 코드를 다시 천천히 읽어보면서 진행합니다.
2차 리팩토링은 피어리뷰를 통해 전달받은 다양한 의견과 피드백을 조율하여 진행합니다. 