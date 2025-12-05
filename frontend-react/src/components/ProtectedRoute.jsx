import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { useUser } from '../hooks/useUser';

const ProtectedRoute = () => {
    const { isLoggedIn, loading } = useUser();

    // 세션 확인 중에는 렌더링을 보류하여 리디렉션을 방지
    if (loading) {
        return <div>Loading...</div>; // 또는 스피너 컴포넌트
    }

    if (!isLoggedIn) {
        alert('로그인이 필요합니다.');
        return <Navigate to="/login" replace />;
    }

    return <Outlet />;
};

export default ProtectedRoute;