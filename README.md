# 🚀 2025 'zest' Intern Board Project

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