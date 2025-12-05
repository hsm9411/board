import React, { useState, useEffect, useMemo } from 'react';
import { useNavigate } from 'react-router-dom';
import { fetchCommonCodes, checkIdDuplicate, signup } from '../../api/userApi';
import styles from './SignupForm.module.css';

function SignupView() {
    const [form, setForm] = useState({
        userId: '', userName: '', userPw: '', userPwCheck: '',
        userPhone1: '010', userPhone2: '', userPhone3: '',
        userAddr1: '', userAddr2: '', userCompany: ''
    });
    const [errors, setErrors] = useState({});
    const [phoneCodes, setPhoneCodes] = useState([]);
    const [idStatus, setIdStatus] = useState({ checked: false, isDuplicate: false });
    const navigate = useNavigate();

    const validation = useMemo(() => ({
        idFormat: /^[a-zA-Z][a-zA-Z0-9]{3,14}$/.test(form.userId),
        pwLength: form.userPw.length >= 8 && form.userPw.length <= 16,
        pwHasLetter: /[a-zA-Z]/.test(form.userPw),
        pwHasNumber: /[0-9]/.test(form.userPw),
        pwCheck: form.userPw && form.userPw === form.userPwCheck,
    }), [form.userId, form.userPw, form.userPwCheck]);

    useEffect(() => {
        fetchCommonCodes('phone').then(setPhoneCodes).catch(console.error);
    }, []);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setForm(prev => ({ ...prev, [name]: value }));
        if (name === 'userId') {
            setIdStatus({ checked: false, isDuplicate: false });
            setErrors(e => ({ ...e, userId: '' }));
        }
    };

    const handleIdCheck = async () => {
        if (!validation.idFormat) {
            setErrors(e => ({ ...e, userId: '아이디 형식이 올바르지 않습니다.' }));
            return;
        }
        try {
            const { isDuplicate } = await checkIdDuplicate(form.userId);
            setIdStatus({ checked: true, isDuplicate });
            setErrors(e => ({ ...e, userId: isDuplicate ? '이미 사용 중인 아이디입니다.' : '' }));
        } catch (err) {
            // [수정] err 변수를 콘솔에 출력하여 경고를 제거하고 디버깅에 활용합니다.
            console.error("ID 중복 확인 중 오류 발생:", err);
            setErrors(e => ({...e, userId: '아이디 중복 확인 중 오류가 발생했습니다.'}));
        }
    };
    
    const handleSubmit = async (e) => {
        e.preventDefault();
        const newErrors = {};
        if (!validation.idFormat) newErrors.userId = '아이디 형식이 올바르지 않습니다.';
        else if (!idStatus.checked || idStatus.isDuplicate) newErrors.userId = '아이디 중복 확인을 해주세요.';
        if (!form.userName.trim()) newErrors.userName = '이름을 입력해주세요.';
        if (!validation.pwLength || !validation.pwHasLetter || !validation.pwHasNumber) newErrors.userPw = '비밀번호 조건을 모두 만족해야 합니다.';
        if (!validation.pwCheck) newErrors.userPwCheck = '비밀번호가 일치하지 않습니다.';
        if (!form.userPhone2.trim() || !form.userPhone3.trim()) newErrors.userPhone = '전화번호를 모두 입력해주세요.';

        setErrors(newErrors);
        
        if (Object.keys(newErrors).length > 0) {
            alert('입력 내용을 다시 확인해주세요.');
            return;
        }

        try {
            const data = await signup(form);
            if (data.status === 'success') {
                alert(data.message);
                navigate('/login');
            } else {
                setErrors(data.errors || { general: '회원가입에 실패했습니다.' });
            }
        } catch (error) {
            alert(error.response?.data?.message || '회원가입 처리 중 서버 오류가 발생했습니다.');
        }
    };

    return (
        <div className={styles.authContainer}>
            <div className={`${styles.authFormWrapper} ${styles.signup}`}>
                <h2 className={styles.formTitle}>회원가입</h2>
                <form onSubmit={handleSubmit} noValidate>
                    <fieldset>
                        <legend>필수 정보</legend>
                        <div className={styles.formGroup}>
                            <label htmlFor="userId">아이디</label>
                            <div className={styles.inputGroup}>
                                <input type="text" id="userId" name="userId" value={form.userId} onChange={handleChange} maxLength="15" placeholder="영문자로 시작하는 4~15자 영문, 숫자" required />
                                <button type="button" onClick={handleIdCheck} disabled={!form.userId || !validation.idFormat}>중복 확인</button>
                            </div>
                            <ul className={styles.validationList}><li className={validation.idFormat ? styles.valid : ''}>✔ 영문자로 시작하는 4~15자 영문, 숫자</li></ul>
                            <div className={styles.messageBox}>
                                {errors.userId && <p className={styles.errorMessage}>{errors.userId}</p>}
                                {idStatus.checked && !idStatus.isDuplicate && <p className={styles.successMessage}>사용 가능한 아이디입니다.</p>}
                            </div>
                        </div>
                        <div className={styles.formGroup}>
                            <label htmlFor="userName">이름</label>
                            <input type="text" id="userName" name="userName" value={form.userName} onChange={handleChange} maxLength="15" placeholder="한글 5자, 영문 15자 이내" required />
                            <div className={styles.messageBox}>{errors.userName && <p className={styles.errorMessage}>{errors.userName}</p>}</div>
                        </div>
                        <div className={styles.formGroup}>
                            <label htmlFor="userPw">비밀번호</label>
                            <input type="password" id="userPw" name="userPw" value={form.userPw} onChange={handleChange} maxLength="16" placeholder="8~16자 영문, 숫자 조합" required />
                            <ul className={styles.validationList}>
                                <li className={validation.pwLength ? styles.valid : ''}>✔ 8~16자 길이</li>
                                <li className={validation.pwHasLetter ? styles.valid : ''}>✔ 영문자 포함</li>
                                <li className={validation.pwHasNumber ? styles.valid : ''}>✔ 숫자 포함</li>
                            </ul>
                            <div className={styles.messageBox}>{errors.userPw && <p className={styles.errorMessage}>{errors.userPw}</p>}</div>
                        </div>
                        <div className={styles.formGroup}>
                            <label htmlFor="userPwCheck">비밀번호 확인</label>
                            <input type="password" id="userPwCheck" name="userPwCheck" value={form.userPwCheck} onChange={handleChange} maxLength="16" required />
                            <div className={styles.messageBox}>
                                {errors.userPwCheck && <p className={styles.errorMessage}>{errors.userPwCheck}</p>}
                                {form.userPwCheck && validation.pwCheck && <p className={styles.successMessage}>비밀번호가 일치합니다.</p>}
                            </div>
                        </div>
                        <div className={styles.formGroup}>
                            <label>전화번호</label>
                            <div className={styles.phoneGroup}>
                                <select name="userPhone1" value={form.userPhone1} onChange={handleChange}>
                                    {phoneCodes.map(code => <option key={code.codeId} value={code.codeId}>{code.codeName}</option>)}
                                </select>
                                - <input type="text" name="userPhone2" value={form.userPhone2} onChange={handleChange} maxLength="4" placeholder="1234" required />
                                - <input type="text" name="userPhone3" value={form.userPhone3} onChange={handleChange} maxLength="4" placeholder="5678" required />
                            </div>
                            <div className={styles.messageBox}>{errors.userPhone && <p className={styles.errorMessage}>{errors.userPhone}</p>}</div>
                        </div>
                    </fieldset>
                    <fieldset>
                        <legend>추가 정보 (선택)</legend>
                        <div className={styles.formGroup}>
                            <label htmlFor="userAddr1">우편번호</label>
                            <input type="text" id="userAddr1" name="userAddr1" value={form.userAddr1} onChange={handleChange} placeholder="123-456" />
                        </div>
                        <div className={styles.formGroup}>
                            <label htmlFor="userAddr2">주소</label>
                            <input type="text" id="userAddr2" name="userAddr2" value={form.userAddr2} onChange={handleChange} />
                        </div>
                        <div className={styles.formGroup}>
                            <label htmlFor="userCompany">회사명</label>
                            <input type="text" id="userCompany" name="userCompany" value={form.userCompany} onChange={handleChange} />
                        </div>
                    </fieldset>
                    {errors.general && <p className={styles.errorMessage}>{errors.general}</p>}
                    <button type="submit" className={styles.submitButton}>가입하기</button>
                </form>
            </div>
        </div>
    );
}

export default SignupView;