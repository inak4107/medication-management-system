# Pill-Tap Server (시스템 백엔드)

본 리포지토리는 NFC 태그와 로드셀(무게 센서)을 활용한 스마트 복약 관리 시스템의 백엔드 서버입니다. 
스마트 약통의 기기 등록, 복약 스케줄 관리, 그리고 무게 변화 감지를 통한 정확한 복약 기록 저장 기능을 제공합니다.

## Tech Stack

* Language: Java 17
* Framework: Spring Boot 4.0.6
* Database: PostgreSQL 16 (Docker Container)
* Build Tool: Gradle
* Security: Spring Security, JWT
* API Docs: Swagger (Springdoc OpenAPI)

---

## Project Structure

각 도메인 패키지는 유저 인증, 기기 통신, 알림 스케줄링, 센서 데이터 처리 등 독립적인 비즈니스 로직을 담당하도록 응집도를 높여 설계했습니다.

```text
pilltapserver
├── config
│   └── SecurityConfig.java
├── domain
│   ├── auth
│   │   ├── AuthController.java
│   │   ├── AuthService.java
│   │   └── RegisterRequest.java
│   ├── hardware
│   │   ├── Hardware.java
│   │   └── HardwareRepository.java
│   ├── medicationlog
│   │   ├── MedicationLog.java
│   │   └── MedicationLogRepository.java
│   ├── medicationschedule
│   │   ├── MedicationSchedule.java
│   │   └── MedicationScheduleRepository.java
│   ├── tagmapping
│   │   ├── TagMapping.java
│   │   └── TagMappingRepository.java
│   └── user
│       ├── User.java
│       ├── UserRepository.java
│       └── UserService.java
├── global
│   ├── common
│   │   └── ApiResponse.java
│   └── exception
│       ├── CustomException.java
│       ├── ErrorCode.java
│       └── GlobalExceptionHandler.java
└── PillTapServerApplication.java
```

---

## Architectural Decisions (설계 및 기술 도입 배경)
1. 도메인 주도 설계(DDD) 패키지 구조 채택
단순 계층형(Layered) 구조 대신 기능(Domain) 단위로 패키지를 분리하여 도메인 응집도를 향상시키고 변경 영향 범위를 최소화했습니다. 각 도메인별로 작업 구역을 완벽히 격리함으로써, 백엔드 팀원 간의 깃허브 병합 충돌을 방지하고 안정적인 병렬 개발 환경을 구축했습니다.

2. ApiResponse + ErrorCode 글로벌 예외 처리
프론트엔드 및 하드웨어 클라이언트와의 API 통신 규격을 예측 가능하게 통일하기 위한 설계로 비즈니스 예외(CustomException) 발생 시 GlobalExceptionHandler가 일괄 처리하도록 구현하여, Controller 레이어의 응답 책임을 제거하고 핵심 비즈니스 로직과 예외 처리의 관심사 분리를 실현했습니다.

3. JWT(JSON Web Token) 기반 인증 방식
모바일 앱 클라이언트와 하드웨어 기기가 동시에 통신하는 다중 클라이언트 환경을 지원하기 위해 도입했습니다. 서버 메모리에 의존하는 Session 대신 Stateless 인증 구조인 JWT를 채택하여, 향후 트래픽 증가 및 서버 증설 시 Horizontal Scaling에 유연하게 대응할 수 있도록 설계했습니다.

4. Docker를 활용한 데이터베이스 격리
팀원 각자의 로컬 OS 환경(Mac, Windows 등) 차이로 인해 발생하는 데이터베이스 설정 문제를 해결하기 위해 Docker Compose를 통해 전 팀원이 동일한 PostgreSQL 16 환경을 구동하도록하여 로컬 환경 의존성을 제거했습니다.

---

## Local Setup (실행 방법)
### 1. 사전 준비
* Java 17
* Docker & Docker Compose

### 2. DB Setting (Docker)
DB는 Docker를 통해 실행됩니다. 프로젝트 루트 디렉토리에서 아래 명령어를 실행하세요.
```bash
docker-compose up -d
```

### 3. Environment Variables (.env 세팅)
본 프로젝트는 spring-dotenv를 사용하여 환경변수를 관리합니다.
프로젝트 루트 경로에 .env 파일을 생성하고 아래 양식을 채워주세요. (보안상 GitHub에 올리지 않았습니다.)

```env
DB_HOST=127.0.0.1
DB_PORT=5433
DB_NAME=med_management_db
DB_USER=tap
DB_PASSWORD=본인의_DB_비밀번호입력
JWT_SECRET=본인의_랜덤_시크릿키_문자열
SERVER_PORT=8080
```

(DB_PASSWORD와 JWT_SECRET은 템플릿 형태로만 제공합니다. 실제 비밀번호는 팀원 각자 로컬에서 기입해야 합니다.)

### 4. API Docs (Swagger)
서버 실행 후, 아래 주소로 접속하면 API 명세서를 확인할 수 있습니다.

* Swagger UI: `http://localhost:8080/swagger-ui/index.html`