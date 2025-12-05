import apiClient from './apiClient';

/**
 * 사용자 인증 및 회원 관련 모든 API 호출을 관리합니다.
 */

// 로그인 요청
export const login = async (credentials) => {
    const response = await apiClient.post('/api/users/login', credentials);
    return response.data;
};

// 로그아웃 요청
export const logout = async () => {
    const response = await apiClient.post('/api/users/logout');
    return response.data;
};

// 현재 세션 정보 확인
export const checkSession = async () => {
    const response = await apiClient.get('/api/users/session');
    return response.data;
};

// 회원가입 요청
export const signup = async (formData) => {
    const response = await apiClient.post('/api/users/signup', formData);
    return response.data;
};

// 아이디 중복 확인
export const checkIdDuplicate = async (userId) => {
    const response = await apiClient.get(`/api/users/id-check?userId=${userId}`);
    return response.data;
};

// 공통 코드 조회 (전화번호 국번 등)
export const fetchCommonCodes = async (type) => {
    const response = await apiClient.get(`/api/common/codes?type=${type}`);
    return response.data;
};