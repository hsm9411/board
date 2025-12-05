import axios from 'axios';

// Axios 인스턴스 생성
const apiClient = axios.create({
    // [핵심] 스프링 부트 서버의 주소를 기본 URL로 설정합니다.
    baseURL: 'http://localhost:8088', 
    
    // [매우 중요] 세션 기반 인증(쿠키)을 위해 반드시 필요한 옵션입니다.
    withCredentials: true 
});

export default apiClient;