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
 - msg = bbbb 에 대한 호감사유를 외모에서 성격으로 변경합니다.

4. 카카오 로그인이 가능한것 처럼, 네이버 로그인으로도 가입 및 로그인 처리.
- 스프링 OAuth2 클라이언트로 구현해주세요.
- 네이버 로그인으로 가입한 회원의 providerTypeCode : NAVER


#### 체크리스트
- [x] 테스트 케이스 만들기
- [x] 데이터 양방향 매핑 파악
- [x] service에서 RsData를 통한 메세지 전송 처리 -> controller로 rq.historyBack으로 구현가능
- [x] 호감표시 목록 크기 비교를 위한 메소드 찾기 (10이하로 만들기 위해)
- [x] 추가: 코드에 하드코딩하지 않고 yml파일에 설정 저장후 불러오기
- [x] 매핑 파악후, 입력된 인스타 아이디와 비교할 수있는 instaMember 접근하는 방법 찾기
- [x] 호감코드(AttractCode) 비교하기
- [x] 중복방지, 수정하기를 나누는 조건문 작성
- [x] 수정하기 실행시 update문 사용하도록 작성
- [x] 수정후 메세지에 인스타아이니와 호감사유 노출
- [x] 네이버 api사용법 숙지
- [x] 네이버 client id, pw yml파일 등록 
- [ ] 네이버에서 받은 json파일 표시하지 않기


---------------------------------------------------------------------------
#### N주차 미션 요약
[접근 방법]
InstaMember과 LikablePerson에 있는 양방향 관계에서 어떤 방식으로 데이터를 꺼내올지 고민을 했다
1:N 관계로 rq.member.getInstaMember().getFromLikeablePeople()로 접근하면 member -> instamember -> likablePerson 순으로 엔티티에 접근한다. 리스트형태로 가져왔다(1:N)
이 likablePerson에서 다시 getToInstaMember로 하면 likablePerson -> instaMember 로 접근하고 그곳에 저장되어 잇는 username을 가져올 수 있다.

위 방식이면 rq로 리스트를 만든다음에 추가적으로 다른 리스트를 선언하지 않고 리스트 하나로 여러가지 시도를 해볼 수 있다.

case:4 
- rq.member로 InstaMember를 가져올 수 있고 여기서 또 getFromLikeablePeople()로 유저가 좋아하는 호감목록을 가져올 수 있다.
- LikablePerson 리스트 형태인데 여기서 getToInstaMember.getUsername 으로 이름을 가져오고 
- for each 문으로 각각 입력된 Username과 비교해 등록된 인스타아이디인지 확인한다.
- 마찬가지로 AttractCode도 비교한다.
- 둘다 겹칠경우 RsData로 F-4 실패코드를 날린다. -> controller에서 rq.history back으로 처리
case:5 
- member.getInstaMember().getFromLikeablePeople() = 내가 좋아하는 사람들 목록, size()로 크기를 구할 수있다.
- size를 코드에 그대로 입력하는 것이 아니라, yml에 설정을 저장하고 @Configuration으로 관리한다. - chatGPT, 강사님 강의 참고 
case:6
- case:4 에서 수행중 이름은 겹치는데 AttractCode가 겹치지 않을경우 ->수정
- attractCode수정은 entity에 setter를 넣어주어 해결. -> sql구문에 update로 나오는것 확인
- getAttractiveTypeDisplayName을 사용하면 코드가 아닌 이름이 표시된다.
- 이전 AttractCode와 바뀐 AttractCode를 저장해 String.format으로 출력했다.
네이버 로그인
- 네이버 개발자 사이트에서 로그인api 을 가져왔다.
- yml에 정보를 입력했다
- json형태로 정보를 가져 오는데 일반형태로 변환중...


[특이사항]
- api등록시 client id,pw를 제외하고  authorization-uri, token-uri, user-info-uri, user-name-attribute, 
- client-name, redirect-uri, authorization-grant-type, scope, 같은 항목들은 어디에서 가져와야 할지 잘 모르겠다. 공식문서에 있는지 찾아봐야겠다.
- 설정을 한곳에 모아두자. 관리도 편하고 yml파일같은곳에 등록하면 불러오는 방법과 적용하는 방법만 알면 편하다.
- 양방향 매핑을 더 공부해서 쉽게 파악할 수 있도록 하자
- 코드가 기능이 많아 지면 분할하자. 이번엔 참 거짓을 판별하는 코드를 밖으로 나누었다.

참고: [Refactoring]
Refactoring 시 주로 다루어야 할 이슈들에 대해 리스팅합니다.
1차 리팩토링은 기능 개발을 종료한 후, 스스로 코드를 다시 천천히 읽어보면서 진행합니다.
2차 리팩토링은 피어리뷰를 통해 전달받은 다양한 의견과 피드백을 조율하여 진행합니다. 