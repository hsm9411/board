import React from 'react';
import { Link, NavLink, useNavigate, useLocation } from 'react-router-dom';
import { useUser } from '../hooks/useUser';
import styles from './AppHeader.module.css';

function AppHeader() {
    const { isLoggedIn, userName, logout } = useUser();
    const navigate = useNavigate();
    const location = useLocation();

    const handleLogout = async () => {
        await logout();
        if (location.pathname !== '/boards') {
            navigate('/boards');
        }
    };

    return (
        <header className={styles.appHeader}>
            <nav>
                <div className={styles.mainLinks}>
                    <NavLink to="/boards" className={({ isActive }) => isActive ? styles.activeLink : ''}>게시판</NavLink>
                    <NavLink to="/mbti" className={({ isActive }) => isActive ? styles.activeLink : ''}>MBTI 검사</NavLink>
                </div>
                {isLoggedIn ? (
                    <div className={styles.userInfo}>
                        <span>환영합니다, {userName}님!</span>
                        <a href="#" onClick={(e) => { e.preventDefault(); handleLogout(); }}>로그아웃</a>
                    </div>
                ) : (
                    <div className={styles.guestInfo}>
                        <Link to="/login">로그인</Link>
                        <Link to="/signup">회원가입</Link>
                    </div>
                )}
            </nav>
        </header>
    );
}

export default AppHeader;