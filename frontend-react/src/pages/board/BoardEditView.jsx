//`/src/pages/board/BoardEditView.jsx`

import React, { useState, useEffect, useCallback } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { fetchBoardDetail, updateBoard } from '../../api/boardApi';
import { useUser } from '../../hooks/useUser';
import styles from './BoardForm.module.css';

function BoardEditView() {
    const { boardType, boardNum } = useParams();
    const navigate = useNavigate();
    const { user, isLoggedIn } = useUser();

    const [board, setBoard] = useState({ boardTitle: '', boardComment: '', creator: '', boardTypeName: '' });
    const [loading, setLoading] = useState(true);

    const loadBoardData = useCallback(async () => {
        setLoading(true);
        try {
            const fetchedBoard = await fetchBoardDetail(boardType, boardNum);
            if (!isLoggedIn || user.userName !== fetchedBoard.creator) {
                alert('수정 권한이 없습니다.');
                navigate(`/boards/view/${boardType}/${boardNum}`);
                return;
            }
            setBoard(fetchedBoard);
        } catch (error) {
            console.error("게시글 데이터 로딩 실패:", error);
            alert('게시글 정보를 불러오는 데 실패했습니다.');
            navigate(-1);
        } finally {
            setLoading(false);
        }
    }, [boardType, boardNum, isLoggedIn, user, navigate]);

    useEffect(() => {
        loadBoardData();
    }, [loadBoardData]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setBoard(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!board.boardTitle.trim() || !board.boardComment.trim()) {
            alert('제목과 내용을 모두 입력해주세요.');
            return;
        }
        try {
            const boardData = { boardTitle: board.boardTitle, boardComment: board.boardComment };
            await updateBoard(boardType, boardNum, boardData);
            alert('게시글이 성공적으로 수정되었습니다.');
            navigate(`/boards/view/${boardType}/${boardNum}`);
        } catch (error) {
            console.error("게시글 수정 실패:", error);
            alert(error.response?.data?.message || '게시글 수정에 실패했습니다.');
        }
    };

    if (loading) return <p>데이터를 불러오는 중입니다...</p>;

    return (
        <div className={styles.boardFormContainer}>
            <h2>게시글 수정</h2>
            <form onSubmit={handleSubmit}>
                <div className={styles.formGroup}>
                    <label>게시판 종류</label>
                    <input type="text" value={board.boardTypeName} readOnly />
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
                    <button type="button" onClick={() => navigate(`/boards/view/${boardType}/${boardNum}`)}>취소</button>
                    <button type="submit">수정</button>
                </div>
            </form>
        </div>
    );
}

export default BoardEditView;