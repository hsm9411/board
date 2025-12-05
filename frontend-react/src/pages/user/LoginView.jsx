import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useUser } from '../../hooks/useUser'; // 혹은 ../context/UserContext
import styles from './AuthForm.module.css'; // 공용 인증 폼 스타일을 import 합니다.

function LoginView() {
    const [credentials, setCredentials] = useState({ userId: '', userPw: '' });
    const [errorMessage, setErrorMessage] = useState('');
    const { login } = useUser();
    const navigate = useNavigate();

    const handleChange = (e) => {
        const { name, value } = e.target;
        setCredentials(prev => ({ ...prev, [name]: value }));
    };

    const handleLogin = async (e) => {
        e.preventDefault();
        setErrorMessage('');
        if (!credentials.userId || !credentials.userPw) {
            setErrorMessage('아이디와 비밀번호를 모두 입력해주세요.');
            return;
        }

        try {
            await login(credentials);
            navigate('/boards');
        } catch (error) {
            setErrorMessage(error.message || '로그인 처리 중 오류가 발생했습니다.');
        }
    };
    
    return (
        // [수정] 화면 중앙 정렬을 위한 최상위 컨테이너
        <div className={styles.authContainer}>
            {/* [수정] 폼을 감싸는 카드 형태의 UI 래퍼 */}
            <div className={styles.authFormWrapper}>
                <h2 className={styles.formTitle}>로그인</h2>
                <form onSubmit={handleLogin} noValidate>
                    <div className={styles.formGroup}>
                        <label htmlFor="userId">아이디</label>
                        <input 
                            type="text" 
                            id="userId" 
                            name="userId" 
                            value={credentials.userId} 
                            onChange={handleChange} 
                            placeholder="아이디를 입력하세요" 
                            required 
                        />
                    </div>
                    <div className={styles.formGroup}>
                        <label htmlFor="userPw">비밀번호</label>
                        <input 
                            type="password" 
                            id="userPw" 
                            name="userPw" 
                            value={credentials.userPw} 
                            onChange={handleChange} 
                            onKeyUp={(e) => e.key === 'Enter' && handleLogin(e)}
                            placeholder="비밀번호를 입력하세요" 
                            required 
                        />
                    </div>
                    
                    {/* 에러 메시지가 나타나도 레이아웃이 밀리지 않도록 고정 높이를 가진 박스 */}
                    <div className={styles.messageBox}>
                        {errorMessage && <p className={styles.errorMessage}>{errorMessage}</p>}
                    </div>
                    
                    <button type="submit" className={styles.submitButton}>
                        로그인
                    </button>
                </form>
                
                <div className={styles.formFooter}>
                    <Link to="/boards">목록으로</Link>
                </div>
            </div>
        </div>
    );
}

export default LoginView;