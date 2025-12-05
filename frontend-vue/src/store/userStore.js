import { defineStore } from 'pinia';
// [수정] apiClient 대신 userApi에서 함수를 가져옵니다.
import { login as apiLogin, logout as apiLogout, checkSession as apiCheckSession } from '@/api/userApi';

export const useUserStore = defineStore('user', {
    state: () => ({
        user: null,
    }),
    getters: {
        isLoggedIn: (state) => !!state.user,
        userName: (state) => state.user ? state.user.userName : '',
    },
    actions: {
        async login(credentials) {
            try {
                // [수정] 직접 호출 대신 userApi의 login 함수를 사용합니다.
                const data = await apiLogin(credentials);
                if (data.status === 'success') {
                    this.user = data.user;
                    return true;
                } else {
                    throw new Error(data.message || '로그인에 실패했습니다.');
                }
            } catch (error) {
                console.error('로그인 처리 실패:', error);
                throw error;
            }
        },
        async logout() {
            try {
                // [수정] userApi의 logout 함수를 사용합니다.
                await apiLogout();
                this.user = null;
                alert('로그아웃 되었습니다.');
            } catch (error) {
                console.error('로그아웃 API 호출 실패:', error);
            }
        },
        async checkSession() {
            try {
                // [수정] userApi의 checkSession 함수를 사용합니다.
                const data = await apiCheckSession();
                if (data.status === 'success') {
                    this.user = data.user;
                } else {
                    this.user = null;
                }
            } catch (error) {
                console.error('세션 확인 API 호출 실패:', error);
                this.user = null;
            }
        }
    }
});