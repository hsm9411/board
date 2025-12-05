import React, { useState, useEffect, useCallback } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { fetchBoardDetail, deleteBoard } from '../../api/boardApi';
import styles from './BoardDetailView.module.css';

function BoardDetailView() {
    const { boardType, boardNum } = useParams();
    const navigate = useNavigate();
    const [board, setBoard] = useState(null);
    const [loading, setLoading] = useState(true);

    const loadBoardDetail = useCallback(async () => {
        setLoading(true);
        try {
            const data = await fetchBoardDetail(boardType, boardNum);
            setBoard(data);
        } catch (error) {
            console.error("게시글 상세 정보 로딩 실패:", error);
            alert('게시글을 불러오는 데 실패했습니다.');
        } finally {
            setLoading(false);
        }
    }, [boardType, boardNum]);

    useEffect(() => {
        loadBoardDetail();
    }, [loadBoardDetail]);

    const handleDelete = async () => {
        if (window.confirm("정말로 이 게시글을 삭제하시겠습니까?")) {
            try {
                await deleteBoard(boardType, boardNum);
                alert('게시글이 삭제되었습니다.');
                navigate('/boards');
            } catch (error) {
                console.error("게시글 삭제 실패:", error);
                alert('게시글 삭제에 실패했습니다.');
            }
        }
    };
    
    if (loading) return <p>게시글을 불러오는 중입니다...</p>;
    if (!board) return <p>해당 게시글을 찾을 수 없습니다.</p>;

    return (
        <div className={styles.boardDetail}>
            <h2 className={styles.boardTitle}>{board.boardTitle}</h2>
            <div className={styles.metaInfo}>
                <span>작성자: {board.creator}</span>
            </div>
            <p className={styles.comment}>{board.boardComment}</p>
            <div className={styles.buttonGroup}>
                {/* [수정] 불필요한 className 제거 */}
                <button onClick={() => navigate('/boards')}>
                    목록으로
                </button>
                {board.isOwner && (
                    <div className={styles.ownerButtons}>
                        {/* [수정] 불필요한 className 제거 */}
                        <button onClick={() => navigate(`/boards/edit/${boardType}/${boardNum}`)}>수정</button>
                        <button onClick={handleDelete}>삭제</button>
                    </div>
                )}
            </div>
        </div>
    );
}

export default BoardDetailView;