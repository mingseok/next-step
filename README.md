# 기본기 되짚기

Java와 Spring Boot의 **기본 개념을 다시 확인하고, 코드로 검증한 기록**을 모아둔 저장소입니다.

프로젝트를 진행하며 “왜 이렇게 동작하는지”, “이 개념이 실제로 뭘 해결하는지”  

흐릿하게 넘어갔던 부분들을 하나씩 정리합니다.

> 면접 준비를 하면서 나는 오랫동안 ‘정답’을 외우는 데에만 집중했다.  
> 질문이 나오면 그에 맞는 문장을 빠르게 말하는 연습을 반복했다.
> 
> 시간이 지날수록, 외운 지식은 금방 잊혔고  
> ‘왜 이런 기술이 필요한가’를 이해하지 못한 채 겉만 따라가고 있었다.
>
> 이제는 어떤 기술을 배울 때마다 항상 “왜?” 를 먼저 묻는다.

<br/>

## 내용 정리

각 항목은 관련 개념 정리와 코드 예제로 구성되어 있습니다.

- [x] [Gradle 빌드 흐름과 JUnit 테스트 구조](https://regal-receipt-228.notion.site/Gradle-JUnit-298f4719c4838174b690fc15a7206d9f?source=copy_link)
- [x] [기본 웹서버 구현 & 고민](https://regal-receipt-228.notion.site/Next-Step-3-29df4719c483807e9d19ffe9e97957b0?source=copy_link) 
- [x] [웹서버 구현하며 받은 피드백](https://regal-receipt-228.notion.site/26-02-28-310f4719c48380f3a01bcba0a4e0d448?source=copy_link)
- [x] [HTTP 이해하기](https://regal-receipt-228.notion.site/Next-Step-4-HTTP-31af4719c48380f689b9e7833536c061?source=copy_link)
- [ ] []()




<br/>

## Java HTTP 웹서버

1. 회원가입 (http://localhost:8080/user/form.html)

   - 회원가입 완료 후 /index.html로 리다이렉트되면 성공.

2. 로그인 (http://localhost:8080/user/login.html)
   - 로그인 성공 → /index.html로 이동
   - 로그인 실패 → /user/login_failed.html로 이동

3. 사용자 목록 확인 (http://localhost:8080/user/list)
   - 로그인 상태 → 가입된 사용자 목록 출력 
   - 비로그인 상태 → /user/login.html로 리다이렉트