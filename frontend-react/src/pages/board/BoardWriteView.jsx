import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { fetchBoardTypes, writeBoard } from '../../api/boardApi';
import styles from './BoardForm.module.css';

function BoardWriteView() {
    const [board, setBoard] = useState({ boardType: '', boardTitle: '', boardComment: '' });
    const [boardTypes, setBoardTypes] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        const loadBoardTypes = async () => {
            try {
                const types = await fetchBoardTypes();
                setBoardTypes(types);
                if (types.length > 0) {
                    setBoard(b => ({ ...b, boardType: types[0].codeId }));
                }
            } catch (error) {
                console.error("게시판 종류 로딩 실패:", error);
            }
        };
        loadBoardTypes();
    }, []);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setBoard(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!board.boardType || !board.boardTitle.trim() || !board.boardComment.trim()) {
            alert('모든 필드를 입력해주세요.');
            return;
        }
        try {
            await writeBoard(board);
            alert('게시글이 성공적으로 등록되었습니다.');
            navigate('/boards');
        } catch (error) {
            console.error("게시글 등록 실패:", error);
            alert(error.response?.data?.message || '게시글 등록에 실패했습니다.');
        }
    };

    return (
        <div className={styles.boardFormContainer}>
            <h2>새 게시글 작성</h2>
            <form onSubmit={handleSubmit}>
                <div className={styles.formGroup}>
                    <label htmlFor="boardType">게시판 종류</label>
                    <select id="boardType" name="boardType" value={board.boardType} onChange={handleChange} required>
                        <option disabled value="">게시판을 선택하세요</option>
                        {boardTypes.map(type => (
                            <option key={type.codeId} value={type.codeId}>{type.codeName}</option>
                        ))}
                    </select>
                </div>
                <div className={styles.formGroup}>
                    <label htmlFor="boardTitle">제목</label>
                    <input type="text" id="boardTitle" name="boardTitle" value={board.boardTitle} onChange={handleChange} required />
                </div>
                <div className={styles.formGroup}>
                    <label htmlFor="boardComment">내용</label>
                    <textarea id="boardComment" name="boardComment" value={board.boardComment} onChange={handleChange} rows="10" required></textarea>
                </div>
                <div className={styles.buttonGroup}>
                    <button type="button" onClick={() => navigate(-1)}>취소</button>
                    <button type="submit">작성</button>
                </div>
            </form>
        </div>
    );
}

export default BoardWriteView;