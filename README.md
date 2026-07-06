# sooRyeo (수료 대학교 학사관리 시스템)

Spring MVC 기반 대학교 LMS(학사관리시스템)입니다. 관리자 / 교수 / 학생 세 가지 역할별로 강의, 수강신청, 시험, 성적, 증명서 발급, 실시간 채팅 등의 기능을 제공합니다.

## 기술 스택

- Java 11, Spring Framework 5.3.22 (Spring MVC), Maven (war 패키징)
- MyBatis + Oracle Database (ojdbc8), `commons-dbcp2`(커넥션풀), `log4jdbc-remix`(SQL 로깅)
- MongoDB (`spring-data-mongodb`) : 채팅 메시지 및 접속 로그 저장
- Spring WebSocket : 실시간 채팅
- Apache Tiles : 뷰 레이아웃
- iText (`kernel`, `layout`, `forms`, `barcodes`, `sign`, `pdfa`) : 증명서 PDF 생성
- Apache POI : 엑셀 다운로드
- Thumbnailator : 이미지 리사이징
- Lombok, Gson, Jackson

## 프로젝트 구조

**sooRyeo_uni_lms/src/main/java/com/sooRyeo/app** (최상위)
- `HomeController.java` : 로그인(학생/교수/관리자), 로그아웃, 아이디/비밀번호 찾기
- `ServerTimeController.java` : 서버 시간 제공
- `WeatherController.java` : 외부 날씨 XML을 JSON으로 변환해 제공
- `TestController.java` : 테스트용 컨트롤러

**.../app/controller** - 도메인별 컨트롤러
- `AdminController.java` : 관리자 대시보드, 회원/커리큘럼/개설과목/통계 관리
- `StudentController.java` : 학생 대시보드, 내정보, 강의/수강신청, 과제, 시간표
- `ProfessorController.java` : 교수 대시보드, 내정보, 담당강의, 과제, 공지
- `ExamController.java` : 시험 출제/응시/채점/결과 조회
- `LectureController.java` : 강의 자료 재생/다운로드, 강의 알림
- `BoardController.java` : 강의 공지, 전체 공지(announcement) 게시판
- `CertificateController.java` : 성적/재학/졸업 증명서 발급 (PDF)
- `ScheduleController.java` : 일정/상담 등록·수정·삭제, 상담 승인
- `SearchController.java` : 단어 검색
- `TimeTableRestController.java` : 시간표 REST API

**.../app/excel/controller/ExcelDownloadViewController.java** - 엑셀 다운로드

**.../app/mongo** - 채팅/로그 (MongoDB)
- `controller` : ChatController, ChatWebSocketHandler, LogController
- `dto`, `entity`, `repository`, `service`, `handshaker`

**.../app/domain** - VO/엔티티 (Student, Professor, Admin, Course, Curriculum, Department, Lecture, Exam, ExamResult, Grade, Assignment, AssignmentSubmit, Attendance, Certificate, Consult, Schedule, RegisteredCourse, TimeTable 등)

**.../app/dto, model, jsonBuilder** - 전송용 DTO 및 JSON 응답 빌더

**.../app/mapper** - MyBatis 매퍼 XML/인터페이스 (admin, student, professor, course, curriculum, department, lecture, board, certificate, attendance, schedule, search, find, test)

**.../app/common, config, aop, ExceptionHandler** - 공통 설정, AOP, 전역 예외 처리

**sooRyeo_uni_lms/src/main/webapp/WEB-INF**
- `web.xml`
- `spring/appServlet/servlet-context.xml` : Spring MVC 설정
- `spring/root-context.xml` : 공통 빈(DataSource 등) 설정
- `spring/config/websocketContext.xml` : WebSocket 설정
- `spring/mongo-connection.xml` : MongoDB 연결 설정
- `sql` : 팀원별 작업 SQL 스크립트 (kimkh, kmj, leejungyeon, shj, syg)
- `tiles`, `views` : 화면 레이아웃 및 JSP

## 주요 기능

### 공통
- 학생 / 교수 / 관리자 로그인, 로그아웃
- 아이디 · 비밀번호 찾기 (이메일 인증)
- 서버 시간 표시, 날씨 위젯

### 학생
- 대시보드, 내 정보 조회/수정 (연락처·이메일 중복확인)
- 강의 목록 조회, 수강신청/수강취소, 시간표 조회
- 과제 목록/상세 조회, 첨부파일과 함께 댓글(질의응답) 등록
- 시험 응시, 결과 조회, 대기 화면
- 성적·재학·졸업 증명서 발급 (PDF)
- 강의자료 다운로드, 강의 알림 등록/해제
- 일정관리(상담 신청 등)
- 실시간 채팅

### 교수
- 대시보드, 강의 개설 요청
- 내 정보 조회/수정
- 담당 강의 목록/상세, 강의자료 업로드
- 과제 등록/수정/삭제/상세조회
- 시험 문제 등록/수정, 응시 결과 조회
- 상담 일정 승인
- 강의 공지사항 작성/수정/삭제
- 실시간 채팅

### 관리자
- 대시보드, 회원(학생/교수) 조회 및 등록
- 커리큘럼 등록/수정/삭제
- 개설과목 등록/수정/삭제/조회
- 학과별 학생 수 등 통계 조회
- 교수별 시간표 조회
- 전체 공지사항 등록/수정/삭제
- 접속/사용 로그 통계 차트 (MongoDB 기반)

### 기타
- 엑셀 다운로드 (수강생/성적 등)
- 실시간 채팅 (WebSocket + MongoDB 저장), 채팅방 생성/삭제, 채팅 알림

## 데이터베이스

- Oracle DB : MyBatis 매퍼(`app/mapper`)로 SQL 관리, DDL/DML은 `sooRyeo_uni_lms/src/main/webapp/WEB-INF/sql`에 팀원별로 분리되어 있음
- MongoDB : 채팅 메시지 및 접속 로그 저장 (`app/mongo`)

## 실행 방법

1. Oracle DB를 준비하고 `WEB-INF/sql`의 스크립트로 테이블을 생성합니다.
2. MongoDB를 설치하고 `spring/mongo-connection.xml`의 연결 정보를 맞춥니다.
3. `spring/root-context.xml` 등에서 DataSource 접속 정보를 확인/수정합니다.
4. Maven으로 빌드한 뒤 Tomcat 등 WAS에 war로 배포합니다.
5. `spring/config/websocketContext.xml` 설정을 확인해 실시간 채팅이 정상 동작하는지 확인합니다.

- 빌드 산출물 디렉터리(`target`)가 저장소에 함께 커밋되어 있어 정리가 필요합니다.
- 프론트엔드 라이브러리(Highcharts, node_modules 등)가 리소스로 포함되어 있어 저장소 용량이 큽니다.
