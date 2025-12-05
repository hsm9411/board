import React, { createContext, useState, useMemo, useCallback } from 'react';
import { login as apiLogin, logout as apiLogout, checkSession as apiCheckSession } from '../api/userApi';

// Context 객체만 export 합니다.
// eslint-disable-next-line react-refresh/only-export-components
export const UserContext = createContext();

// Provider 컴포넌트를 export 합니다.
export function UserProvider({ children }) {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);

    const isLoggedIn = useMemo(() => !!user, [user]);
    const userName = useMemo(() => user ? user.userName : '', [user]);

    const login = useCallback(async (credentials) => {
        const data = await apiLogin(credentials);
        if (data.status === 'success') {
            setUser(data.user);
            return true;
        } else {
            throw new Error(data.message || '로그인에 실패했습니다.');
        }
    }, []);

    const logout = useCallback(async () => {
        try {
            await apiLogout();
            setUser(null);
            alert('로그아웃 되었습니다.');
        } catch (error) {
            console.error('로그아웃 API 호출 실패:', error);
        }
    }, []);

    const checkSession = useCallback(async () => {
        setLoading(true);
        try {
            const data = await apiCheckSession();
            setUser(data.status === 'success' ? data.user : null);
        } catch (error) {
            console.error('세션 확인 API 호출 실패:', error);
            setUser(null);
        } finally {
            setLoading(false);
        }
    }, []);

    const value = { user, isLoggedIn, userName, loading, login, logout, checkSession };

    return (
        <UserContext.Provider value={value}>
            {children}
        </UserContext.Provider>
    );
}