# board
2025 'zest' intern board project

## 📋 README.md (초안)

```markdown
# 🚀 Spring Legacy Board Project (feat. React & Vue)

Spring Legacy (MVC) 기반의 백엔드 API 시스템과 최신 프론트엔드 기술(React 19, Vue 3)을 접목시킨 풀스택 웹 애플리케이션 프로젝트입니다. 
전통적인 서버 사이드 렌더링 방식에서 벗어나 **RESTful API 아키텍처**로 전환하고, 두 가지 메이저 프론트엔드 라이브러리의 구현 방식을 비교/학습하기 위해 제작되었습니다.

## 📚 Tech Stack

### Backend
<img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white"> <img src="https://img.shields.io/badge/Spring_Legacy-6DB33F?style=for-the-badge&logo=spring&logoColor=white"> <img src="https://img.shields.io/badge/Oracle-F80000?style=for-the-badge&logo=oracle&logoColor=white"> <img src="https://img.shields.io/badge/MyBatis-000000?style=for-the-badge&logo=mybatis&logoColor=white"> <img src="https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white"> <img src="https://img.shields.io/badge/Tomcat-F8DC75?style=for-the-badge&logo=apachetomcat&logoColor=black">

*   **Framework**: Spring Framework (MVC)
*   **Database**: Oracle Database 11g/XE
*   **ORM**: MyBatis
*   **Build Tool**: Maven
*   **Was**: Apache Tomcat 9.0

### Frontend (Option 1: React)
<img src="https://img.shields.io/badge/React_19-61DAFB?style=for-the-badge&logo=react&logoColor=black"> <img src="https://img.shields.io/badge/Vite-646CFF?style=for-the-badge&logo=vite&logoColor=white"> <img src="https://img.shields.io/badge/React_Router-CA4245?style=for-the-badge&logo=reactrouter&logoColor=white">

*   **Core**: React v19.1.1
*   **Build Tool**: Vite
*   **Routing**: React Router DOM v7

### Frontend (Option 2: Vue)
<img src="https://img.shields.io/badge/Vue.js_3-4FC08D?style=for-the-badge&logo=vuedotjs&logoColor=white"> <img src="https://img.shields.io/badge/Pinia-FFE80D?style=for-the-badge&logo=pinia&logoColor=black"> <img src="https://img.shields.io/badge/Vuex-4FC08D?style=for-the-badge&logo=vue.js&logoColor=white">

*   **Core**: Vue v3.2
*   **State Management**: Pinia, Vuex (Migration/Comparison)
*   **Build Tool**: Vue CLI

---

## 📂 Project Structure

```text
📦 Project Root
 ┣ 📂 backend            # Spring Legacy Backend (API Server)
 ┃ ┣ 📂 src/main/java    # Controller, Service, DAO, VO (Business Logic)
 ┃ ┣ 📂 src/main/resources/sql  # MyBatis Mapper XMLs
 ┃ ┗ 📂 src/main/webapp  # WEB-INF (Settings: web.xml, root-context, etc)
 ┣ 📂 frontend-react     # React Client (Vite based)
 ┗ 📂 frontend-vue       # Vue Client (CLI based)
```

## ✨ Key Features

1.  **게시판 (Board)**
    *   Spring MVC 패턴을 준수한 게시글 CRUD
    *   REST API 설계를 통한 프론트엔드 데이터 통신
2.  **사용자 (User)**
    *   회원가입 및 로그인 프로세스
3.  **MBTI 테스트 (MBTI)**
    *   성격 유형 검사 로직 및 결과 산출
    *   React/Vue 각 프레임워크별 상태 관리 및 UI 구현 비교
4.  **기술적 특징**
    *   **CORS 처리**: `SimpleCORSFilter`를 통해 로컬 환경(Port 8080 <-> 8081/5173) 간 통신 허용
    *   **DB 연동**: `Log4jdbc`를 적용하여 쿼리 로그 가시성 확보

---

## ⚙️ Installation & Setup

### 1. Database Setup (Oracle)
*   로컬 Oracle DB에 아래 계정을 생성해야 합니다. (설정 파일 기준)
    *   **Username**: `TEST2`
    *   **Password**: `test2`
    *   **URL**: `jdbc:oracle:thin:@localhost:1521:xe`
*   *설정을 변경하려면 `backend/src/main/webapp/WEB-INF/spring/root-context.xml` 파일을 수정하세요.*

### 2. Backend Run
1.  `backend` 프로젝트를 IDE(Eclipse/IntelliJ)로 Import (Maven Project).
2.  **Apache Tomcat 9.0** 서버 설정 후 `backend` 모듈을 Add.
3.  서버 실행 (기본 포트: `8080`).

### 3. Frontend Run

#### React (Vite)
```bash
cd frontend-react
npm install
npm run dev
# 접속: http://localhost:5173
```

#### Vue (CLI)
```bash
cd frontend-vue
npm install
npm run serve
# 접속: http://localhost:8081
```

---

## 📝 Configuration Note

*   **API Proxy (CORS)**
    *   백엔드의 `web.xml` 및 `SimpleCORSFilter` 설정을 통해 `/api/*` 경로로 들어오는 요청에 대해 Cross-Origin 요청을 허용하고 있습니다.
*   **MyBatis Mapper**
    *   SQL 매퍼 파일들은 `src/main/resources/sql` 하위의 `*_sql.xml` 패턴을 따릅니다.

## 🤝 Contributing
이 프로젝트는 개인 학습 및 포트폴리오 목적으로 제작되었습니다. 이슈나 개선 사항은 PR로 남겨주세요.
```

---

### 💡 작성자가 덧붙이는 팁 (README 반영 후 할 일)

1.  **보안 주의 (중요):**
    *   `root-context.xml`에 DB 아이디(`TEST2`)와 비밀번호(`test2`)가 그대로 노출되어 있습니다.
    *   실제 Git에 올릴 때는 이 부분을 `db.properties` 파일로 분리하고 `.gitignore`에 등록하거나, README에 **"보안을 위해 비밀번호는 삭제 처리했습니다. 본인 환경에 맞게 수정하세요"**라고 적어두는 것이 좋습니다.
2.  **Vue 상태관리:**
    *   `package.json`에 **Pinia**와 **Vuex**가 동시에 설치되어 있습니다. 둘 다 사용하는 것이 의도된 것인지(마이그레이션 중 등), 아니면 하나만 쓰는데 의존성이 남은 것인지 확인해보시면 좋습니다. (README에는 일단 둘 다 적어두었습니다.)
3.  **React 버전:**
    *   React 19 (Beta/RC 버전 포함)와 React Router v7을 사용 중이십니다. 굉장히 최신 스택이므로, 이를 "최신 기술 도입"이라는 장점으로 어필하기 좋습니다.