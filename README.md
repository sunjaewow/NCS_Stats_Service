### 🌏NCSprojects

노원천문우주과학관의 티켓 시스템을 아날로그에서 디지털로 바꾸는 서비스

개발기간 : 2025.02.01 ~ 2025.3.18

역할 : 통계 서비스 담당

기술 스택 : Spring Boot, Apache PoI, Spring Cloud Netflix Eureka, MySQL

---

### 📌 프로젝트 소개

노원천문우주과학관의 티켓 시스템을 아날로그에서 디지털로 바꾸는 서비스중에 예약 데이터를 월별로 정리된 엑셀 파일로 다운로드 할 수 있는 서비스 개발

---

### 📐 아키텍처


<img width="1005" height="413" alt="Image" src="https://github.com/user-attachments/assets/a446c71a-6860-4e3b-aba8-b4c870bf8a7a" />

- 예약 데이터를 월별로 정리된 엑셀 파일로 다운로드할 수 있는 백엔드 API를 개발.
- 사용자가 특정 콘텐츠 ID로 요청하면, 해당 콘텐츠의 월별 예약 현황 데이터를 조회하여 Apache POI를 사용해 동적으로 엑셀 파일을 생성하고, HTTP 응답으로 파일 다운로드를 지원하는 기능을 구현.
- 엑셀 파일은 월별 시트를 생성하고 각 시트에는 날짜별, 시간대별, 대인/소인 예약 수가 표 형태로 표시되도록 구성.
- 서비스 간 통신은 gRPC를 사용하고, 서비스 등록과 검색은 Netflix Eureka를 통해 관리하는 MSA환경에서 개발.
- 이를 통해 사용자는 실시간 예약 현황을 직관적으로 파악할 수 있으며, 데이터 시각화와 관리의 편의성을 높임.

---

### 📘 문서

- [포트폴리오 (Notion)](https://www.notion.so/NCSprojects-1ebefd57b2f780d3899ff8afdc1eb4c6?source=copy_link)
