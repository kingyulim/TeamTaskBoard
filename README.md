# Team Task Board Backend

팀 기반 작업(Task) 관리 시스템의 백엔드 서버
사용자 인증(JWT), 팀 협업, 작업 관리, 댓글, 활동 로그, 대시보드, 검색 기능을 제공합니다.

---

## 기술 스택

- Java 17

- Spring Boot

- Spring Security + JWT

- Spring Data JPA

- MySQL

- Docker

- Lombok

- Hibernate Auditing

---

##  프로젝트 아키텍처 개요
```text
common
├─ aspect        (ActivityLogAop)
├─ config        (Security, PasswordEncoder, UserDetails)
├─ dto           (ApiResponse)
├─ jwt           (JWT 생성/검증)
├─ exception     (공통 예외 처리)
├─ entity        (공통 Entity)
└─ regexp        (정규식 관리)

domain
├─ user          (회원, 인증)
├─ team          (팀, 팀 멤버)
├─ task          (작업 관리)
├─ comment       (댓글/대댓글)
├─ activity      (활동 로그)
├─ dashboard     (통계)
└─ search        (통합 검색)
```

---
## 인증 & 보안 설계 (User 도메인)

### 1️⃣ 회원가입

- 입력값 검증 (@Valid)

- 정규식은 RegExp 클래스로 공통 관리

- 아이디/이메일 중복 체크

- 비밀번호 암호화 후 저장

### 2️⃣ 로그인 (JWT 발급)

- 아이디 존재 여부 확인

- Soft Delete 계정 차단

- 비밀번호 검증 후 JWT 발급

### 3️⃣ 권한 관리 (UserRoleEnum)
```text
USER  → ROLE_NORMAL
ADMIN → ROLE_ADMIN
```
- Spring Security 권한 이름과 매핑
- 추후 관리자 기능 확장 고려

### 4️⃣ 사용자 정보 관리
- 기능설명

  | 기능     | 설명              |
  | ------ | --------------- |
  | 사용자 조회 | 삭제되지 않은 사용자만 조회 |
  | 정보 수정  | 본인만 가능          |
  | 회원 탈퇴  | Soft Delete 적용  |

✔ 실제 데이터 삭제 ❌

✔ 활동 로그 / 통계 데이터 보존

---

## Team 도메인 (팀 & 멤버)
### 🔹 설계 특징

- User – Team 다대다 관계

- 중간 엔티티 UserTeams 사용
```text
User  ---< UserTeams >---  Team
```

### 주요 기능

- 팀 생성 / 수정 / 삭제

- 팀 멤버 추가 / 삭제

- 팀 멤버만 수정 가능

- 멤버가 있는 팀은 삭제 불가

## Task 도메인 (작업 관리 핵심)
### 🔹 기능

- 작업 생성 / 조회 / 수정 / 삭제

- 상태 변경 (PATCH)

- 담당자 기반 권한 검증

- Soft Delete 적용

### 조회 설계

- @EntityGraph 사용 → N+1 문제 방지

- 상태 / 담당자 / 키워드 동적 필터링

- DTO 분리 전략

| 상황    | 응답         |
| ----- | ---------- |
| 목록 조회 | 간단한 담당자 정보 |
| 단건 조회 | 상세 담당자 정보  |

---

## Comment 도메인 (댓글 & 대댓글)
### 🔹 특징

- 부모/자식 구조 지원

- 부모 댓글 기준 페이징

- 대댓글이 많아도 페이지 깨지지 않음

- 작성자만 수정/삭제 가능

- Soft Delete 적용
---

## Activity 도메인 (활동 로그)
### 🔹 목적

- Task / Comment 관련 모든 사용자 행동 기록

- 대시보드 통계 및 히스토리 조회에 활용

### ActivityTypeEnum 설계
```
TASK_CREATED("saveTask", "작업 생성") {
    public String apply(String... str) {
        return String.format("새로운 작업 \"%s\"을 생성했습니다.", str[0]);
    }
}
```

---

## Dashboard 도메인
### 제공 정보

- 전체 작업 수

- 완료 작업 수

- 완료율

- 오늘/예정 작업

- 최근 7일 작업 생성 추이

---

## Search 도메인 (통합 검색)

- Task / Team / User 통합 검색

- 하나의 API에서 결과 반환
```json
{
"tasks": [],
"teams": [],
"users": []
}
```

---

## 데이터 관리 전략
✔ Soft Delete

- User / Task / Comment에 적용

- isDeleted = false 조건으로 조회 제한

✔ Auditing
- @EnableJpaAuditing

- 생성일 / 수정일 자동 관리

- 모든 주요 Entity에 공통 적용

---

## 환경 설정
```text
spring:
datasource:
url: ${DB_URL}
username: ${DB_USERNAME}
password: ${DB_PASSWORD}

jwt:
secret:
key: ${JWT_KEY}
```
Docker 환경에서도 동일하게 사용 가능

---

## 프로젝트 설계 총평

- 본 프로젝트는 팀 기반 협업을 중심으로
사용자 인증(JWT), 작업 관리, 댓글 구조, 활동 로그,
대시보드 통계 및 통합 검색 기능을 도메인 단위로 설계·구현했습니다.

- 공통 로직과 비즈니스 로직을 분리하고
Soft Delete, DTO 분리, 권한 검증 등
실무 환경을 고려한 구조를 적용했습니다.
