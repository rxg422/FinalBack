# JBRO Backend Progress Log

파이널 프로젝트 진행과 학습 내용을 날짜별로 정리하는 문서다.
DB 비밀번호, RDS 접속 암호 같은 민감정보는 기록하지 않는다.

## 2026-05-08

### 진행한 내용

- Spring Boot 백엔드 프로젝트 `JBRO` 구조를 확인했다.
- `src/main/resources/application.properties` 파일이 공통 설정 파일임을 확인했다.
- 개인 DB 접속정보를 분리하기 위해 아래 파일을 추가했다.
  - `src/main/resources/application-local.properties`
  - `src/main/resources/application-local.example.properties`
- `application.properties`에서 local 설정 파일을 불러오도록 설정했다.

```properties
spring.config.import=optional:classpath:application-local.properties
```

- `.gitignore`에 local 설정 파일을 추가해 DB 접속정보가 Git에 올라가지 않도록 처리했다.

```gitignore
src/main/resources/application-local.properties
```

- Oracle JDBC 의존성을 주석 처리하고 MySQL JDBC 의존성을 추가했다.

```xml
<dependency>
	<groupId>com.mysql</groupId>
	<artifactId>mysql-connector-j</artifactId>
	<scope>runtime</scope>
</dependency>
```

- AWS RDS MySQL 접속 설정을 `application-local.properties`에 적용했다.
- 8080 포트 충돌이 발생해 local 설정에서 서버 포트를 8081로 변경했다.

```properties
server.port=8081
```

- DB 연결 확인용 API를 만들었다.

```http
GET /api/health/db
```

- 테이블 목록 확인용 API를 만들었다.

```http
GET /api/health/tables
GET /api/health/tables/{tableName}
```

### 막혔던 내용

- Spring Boot 실행 시 `Port 8080 was already in use` 오류가 발생했다.
- 기존에 실행 중인 서버가 8080 포트를 사용하고 있어 새 서버가 실행되지 않았다.
- Maven wrapper 실행 중 `Cannot index into a null array` 오류가 발생했다.
- Maven local repository 접근 권한 문제도 발생했다.
- Spring Security 때문에 `/` 접근 시 기본 로그인 화면으로 이동했다.

### 해결한 내용

- `server.port=8081`로 포트를 변경해 8080 충돌을 피했다.
- `mvnw.cmd`의 null 처리 부분을 수정해 Maven wrapper가 실행되도록 했다.
- 필요한 경우 관리자 권한으로 기존 Spring Boot 프로세스를 종료했다.
- `/api/health/**` 경로는 로그인 없이 접근할 수 있도록 Security 설정을 추가했다.
- `/api/health/db` 호출 결과로 MySQL 연결과 `SELECT 1` 쿼리 성공을 확인했다.

### 학습한 내용

- DB 인스턴스 식별자와 데이터베이스 이름은 다르다.
- RDS endpoint는 DB 서버 주소이고, Spring Boot와 DBeaver가 이 주소로 접속한다.
- `application-local.properties`는 개인 환경 설정 파일이므로 Git에 올리지 않는다.
- `8080`, `8081`, `8082`는 Spring Boot 웹 서버 포트이고, MySQL 포트 `3306`과 다르다.
- Spring Security 기본 로그인 화면의 계정은 DB 계정과 다르다.

## 2026-05-11

### 진행한 내용

- 현재 실행 중인 서버 포트를 확인했다.
- `8081`은 사용 중이 아니고, `8082`에서 서버가 실행 중임을 확인했다.
- MySQL Workbench/DBeaver의 connection 개념을 정리했다.
  - local connection은 내 PC의 MySQL 서버
  - aws connection은 AWS RDS MySQL 서버
- DB와 API의 차이를 정리했다.
  - DB는 데이터 저장소
  - API는 프론트엔드가 백엔드에 데이터를 요청하는 통로
- 기존 `/api/members` 테스트 CRUD를 MyBatis 구조로 구성했다.

```text
Controller
→ Service(interface)
→ ServiceImpl
→ DAO(interface)
→ Mapper.xml
→ DB
```

- `member` 테이블 기준 CRUD API를 구성했다.

```http
GET    /api/members
GET    /api/members/{memberId}
POST   /api/members
PUT    /api/members/{memberId}
DELETE /api/members/{memberId}
```

- `MemberVO`에 회원 정보 컬럼을 추가했다.
  - `memberId`
  - `email`
  - `password`
  - `nickname`
  - `profile`
  - `createdAt`
  - `status`
- `mypage-mapper.xml`에 `select`, `insert`, `update`, `delete` SQL을 작성했다.
- 프론트엔드 MyPage 작업 컨텍스트를 기준으로 향후 백엔드 API 방향을 정리했다.

### 막혔던 내용

- 8081 포트가 사용 중이라는 오류가 다시 발생했지만, 실제 확인 결과 현재는 8082에서 서버가 실행 중이었다.
- `netstat -ano | findstr :8081` 실행 시 아무것도 나오지 않아 혼란이 있었다.
- Spring Security 로그인 화면에서 입력해야 할 Username/Password가 DB 계정과 헷갈렸다.
- `CREATE DATABASE jbro`가 테이블까지 자동으로 만드는지 혼동이 있었다.
- MySQL에서 `CREATE TABLE USER (...)` 문법 작성 중 `USER` 테이블명과 `SYSDATE()` 사용 방식이 문제가 됐다.

### 해결한 내용

- `netstat -ano | findstr :8082`로 현재 실행 중인 서버를 확인했다.
- 로그인 화면의 계정은 DB 계정이 아니라 Spring Security 기본 인증이라는 점을 정리했다.
- `CREATE DATABASE`는 빈 데이터베이스 공간만 만들고, 테이블은 `CREATE TABLE`로 별도 생성해야 한다는 점을 정리했다.
- MySQL 테이블 생성 문법을 `member` 테이블 기준으로 정리했다.

```sql
CREATE TABLE member (
  member_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  email VARCHAR(200) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  nickname VARCHAR(100) NOT NULL,
  profile VARCHAR(500),
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  status CHAR(1) DEFAULT 'Y'
);
```

### 학습한 내용

- RDS MySQL을 사용한다는 것은 DB를 내 PC가 아니라 AWS 서버에 저장하고 사용하는 것이다.
- 프론트엔드는 DB에 직접 접근하지 않고, Spring Boot API를 통해 데이터를 요청한다.
- MyBatis Mapper는 Java 메서드와 SQL을 연결하는 역할을 한다.
- 마이페이지는 단순 회원 CRUD가 아니라 로그인한 사용자의 정보를 기준으로 동작해야 한다.
- 현재는 로그인 연동 전이므로 학습용으로 `/api/members` CRUD를 먼저 구성하고, 이후 `/api/mypage/...` API로 확장하는 흐름이 적절하다.

### 다음 작업

- `member` 테이블에 더미데이터를 추가한다.
- `/api/members` 응답으로 실제 회원 목록이 나오는지 확인한다.
- 마이페이지용 실제 API를 별도로 설계한다.

```http
GET /api/mypage/profile
PUT /api/mypage/profile
GET /api/mypage/profile/nickname-check
```

- 로그인 기능이 완성되기 전에는 임시로 `memberId = 1`을 기준으로 마이페이지 API를 구현한다.
- 이후 JWT 또는 Spring Security 인증 정보에서 현재 로그인한 회원을 식별하도록 확장한다.

### 추가 진행 내용

- Google Drive의 `TABLE 기술서(초안)_For++ _260430` 문서를 참고해 MySQL용 스키마 파일을 작성했다.

```text
docs/schema-mysql.sql
```

- 소셜 로그인 기준으로 사용자 정보 테이블을 `member`, 소셜 계정 연결 정보를 `social_account`로 분리했다.
- 소셜 로그인만 사용할 예정이므로 `member` 테이블에서 `password` 컬럼을 제거하는 방향으로 정리했다.
- `MemberVO`와 `mypage-mapper.xml`을 새 `member` 테이블 구조에 맞게 수정했다.

```text
기존: member_id, password 사용
변경: id 사용, password 제거
```

- 로그인 기능이 아직 완성되지 않았으므로, 임시 로그인 사용자로 `member id = 1`을 사용하는 마이페이지 프로필 API를 구현했다.

```http
GET /api/mypage/profile
GET /api/mypage/profile/nickname-check?nickname=...
PUT /api/mypage/profile
```

- `PUT /api/mypage/profile` 요청으로 `id = 1` 사용자의 닉네임을 수정하는 흐름을 확인했다.

### 추가 학습 내용

- 실제 로그인 연동 전에도 `memberId = 1`처럼 임시 사용자 값을 고정하면 프론트와 백엔드 API 흐름을 먼저 검증할 수 있다.
- 소셜 로그인에서는 비밀번호를 직접 저장하지 않고, `social_account` 테이블에 `provider`, `provider_id`를 저장해 사용자를 식별한다.
- `localhost` 서버 포트는 `application-local.properties`의 `server.port` 값에 따라 달라진다.

### 프론트엔드 연동 진행 내용

- 프론트엔드 `MyPage` 화면의 회원정보 수정 영역을 백엔드 API와 연결했다.

```text
C:\FinalProject\FrontendWorkspace\JB-ro\src\app\MyPage\page.tsx
```

- 기존에는 `page.tsx` 내부 더미데이터로 닉네임을 관리했지만, 이제 아래 백엔드 API를 호출하도록 수정했다.

```http
GET /api/mypage/profile
GET /api/mypage/profile/nickname-check?nickname=...
PUT /api/mypage/profile
```

- 프론트에서 사용하는 기본 백엔드 주소는 아래처럼 설정했다.

```ts
const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL ?? 'http://localhost:8080';
```

- 마이페이지 진입 시 백엔드에서 프로필 정보를 조회해 사이드 프로필 카드의 닉네임과 이메일을 표시하도록 수정했다.
- 회원정보 수정 메뉴에서 닉네임 중복확인 버튼을 누르면 백엔드 중복확인 API를 호출하도록 수정했다.
- 수정완료 버튼을 누르면 `PUT /api/mypage/profile`로 닉네임을 수정하고, 성공 시 프론트 상태도 갱신하도록 수정했다.

### 프론트엔드 연동 중 막혔던 내용

- 프론트에서 백엔드 API 호출 시 네트워크 에러가 발생했다.
- 백엔드 서버와 프론트 서버는 모두 실행 중이었다.

```text
프론트엔드: http://localhost:3000
백엔드: http://localhost:8080
```

- 원인은 브라우저의 CORS 정책이었다.
- 브라우저는 `localhost:3000`에서 `localhost:8080`으로 요청하는 것을 서로 다른 출처로 판단한다.
- 백엔드가 프론트 출처를 명시적으로 허용하지 않으면 브라우저가 요청을 차단한다.

### 프론트엔드 연동 문제 해결 내용

- 백엔드 `SecurityConfig`에 CORS 설정을 추가했다.

```text
C:\FinalProject\BackendWorkspace\JBRO\src\main\java\com\example\demo\SecurityConfig.java
```

- 허용한 프론트엔드 주소:

```text
http://localhost:3000
http://127.0.0.1:3000
http://192.168.10.29:3000
```

- 허용한 HTTP 메서드:

```text
GET, POST, PUT, PATCH, DELETE, OPTIONS
```

- 백엔드를 재시작한 뒤 아래 요청들이 정상 응답하는 것을 확인했다.

```text
GET /api/mypage/profile
OPTIONS /api/mypage/profile
```

### 오늘 기준 현재 상태

- 백엔드 서버는 `http://localhost:8080` 기준으로 동작한다.
- 프론트엔드 서버는 `http://localhost:3000` 기준으로 동작한다.
- 마이페이지 회원정보 수정의 1차 흐름은 프론트와 백엔드가 연결됐다.
- 현재 로그인 기능은 아직 실제 소셜 로그인과 연결되지 않았고, 백엔드에서 임시 사용자 `id = 1`을 로그인 사용자처럼 사용한다.

### 다음 작업 후보

- 프론트에서 실제로 닉네임 중복확인과 수정완료 버튼을 눌러 화면 반영까지 확인한다.
- `member id = 1` 고정값을 추후 Spring Security/OAuth 로그인 사용자 정보로 교체한다.
- 프로필 이미지 업로드 API를 구현한다.
- 관심 목록 API로 다음 연동 범위를 확장한다.
- `/api/members` 테스트용 CRUD와 `/api/mypage/profile` 실제 마이페이지 API의 역할을 구분해서 정리한다.
