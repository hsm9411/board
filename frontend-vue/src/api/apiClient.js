// /src/api/apiClient.js

import axios from 'axios';

// Axios 인스턴스 생성
const apiClient = axios.create({
    // [핵심] 스프링 부트 서버의 주소를 기본 URL로 설정합니다.
    baseURL: process.env.VUE_APP_API_BASE_URL,
    
    // [매우 중요] 세션 기반 인증(쿠키)을 위해 반드시 필요한 옵션입니다.
    // 이 옵션이 없으면, 브라우저가 세션 쿠키를 서버로 보내지 않아 로그인 상태가 유지되지 않습니다.
    withCredentials: true 
});

export default apiClient;