import React, { useState, useEffect, useMemo, useCallback } from 'react';
import { Link, useNavigate, useSearchParams } from 'react-router-dom';
import { fetchBoardTypes, fetchBoardList } from '../../api/boardApi';
import { useUser } from '../../hooks/useUser';
import BasicTable from '../../components/BasicTable';
import styles from './BoardListView.module.css';

function BoardListView() {
    const [boardList, setBoardList] = useState([]);
    const [pageInfo, setPageInfo] = useState({});
    const [boardTypes, setBoardTypes] = useState([]);
    const [selectedTypes, setSelectedTypes] = useState([]);
    
    const { isLoggedIn } = useUser();
    const navigate = useNavigate();
    const [searchParams, setSearchParams] = useSearchParams();

    const tableHeaders = [
        { text: '타입', key: 'boardTypeName', width: '15%' },
        { text: '번호', key: 'boardNum', width: '10%' },
        { text: '제목', key: 'boardTitle', width: '50%' },
        { text: '작성자', key: 'creator', width: '25%' },
    ];

    const loadBoardList = useCallback(async (pageNo, types) => {
        try {
            const data = await fetchBoardList(pageNo, types);
            setBoardList(data.boardList);
            setPageInfo(data.pageInfo);
            setSearchParams({ page: pageNo });
        } catch (error) {
            console.error("게시글 목록 로딩 실패:", error);
            alert('게시글을 불러오는 데 실패했습니다.');
        }
    }, [setSearchParams]);

    useEffect(() => {
        const initialize = async () => {
            try {
                const types = await fetchBoardTypes();
                setBoardTypes(types);
                const initialSelectedTypes = types.map(t => t.codeId);
                setSelectedTypes(initialSelectedTypes);
                
                const page = searchParams.get('page') || 1;
                loadBoardList(page, initialSelectedTypes);
            } catch (error) {
                console.error("게시판 타입 로딩 실패", error);
            }
        };
        initialize();
    }, [loadBoardList, searchParams]);
    
    const handleSearch = () => {
        loadBoardList(1, selectedTypes);
    };

    const handleTypeChange = (typeId) => {
        setSelectedTypes(prev => 
            prev.includes(typeId) ? prev.filter(id => id !== typeId) : [...prev, typeId]
        );
    };

    const isAllSelected = useMemo(() => boardTypes.length > 0 && selectedTypes.length === boardTypes.length, [boardTypes, selectedTypes]);

    const handleSelectAll = (e) => {
        setSelectedTypes(e.target.checked ? boardTypes.map(t => t.codeId) : []);
    };

    const pageRange = useMemo(() => {
        if (!pageInfo.startPage) return [];
        return Array.from({ length: pageInfo.endPage - pageInfo.startPage + 1 }, (_, i) => pageInfo.startPage + i);
    }, [pageInfo]);

    const goToDetail = (item) => {
        navigate(`/boards/view/${item.boardType}/${item.boardNum}`);
    };

    return (
        <div>
            <h1>게시판 목록</h1>
            <div className={styles.boardInfo}>
                <span>총 게시물: {pageInfo.totalCnt || 0}건</span>
            </div>
            <div className={styles.filterSection}>
                <div className={styles.filterOptions}>
                    <label>
                        <input type="checkbox" checked={isAllSelected} onChange={handleSelectAll} /> 전체
                    </label>
                    {boardTypes.map(type => (
                        <label key={type.codeId}>
                            <input type="checkbox" value={type.codeId} checked={selectedTypes.includes(type.codeId)} onChange={() => handleTypeChange(type.codeId)} /> {type.codeName}
                        </label>
                    ))}
                </div>
                <button onClick={handleSearch} className={styles.searchButton}>검색</button>
            </div>
            
            <BasicTable headers={tableHeaders} items={boardList} onRowClick={goToDetail} />
            
            {pageInfo.totalCnt > 0 && (
                <div className={styles.pagination}>
                    <a href="#" onClick={(e) => { e.preventDefault(); loadBoardList(1, selectedTypes); }}>&laquo;</a>
                    {pageInfo.prev ? <a href="#" onClick={(e) => { e.preventDefault(); loadBoardList(pageInfo.startPage - 1, selectedTypes); }}>&lsaquo;</a> : <span className={styles.disabled}>&lsaquo;</span>}
                    {pageRange.map(page => (
                        <a key={page} href="#" className={page === pageInfo.pageNo ? styles.active : ''} onClick={(e) => { e.preventDefault(); loadBoardList(page, selectedTypes); }}>
                            {page}
                        </a>
                    ))}
                    {pageInfo.next ? <a href="#" onClick={(e) => { e.preventDefault(); loadBoardList(pageInfo.endPage + 1, selectedTypes); }}>&rsaquo;</a> : <span className={styles.disabled}>&rsaquo;</span>}
                    <a href="#" onClick={(e) => { e.preventDefault(); loadBoardList(pageInfo.totalPage, selectedTypes); }}>&raquo;</a>
                </div>
            )}

            <div className={styles.tableFooter}>
                {isLoggedIn && <Link to="/boards/write" className={styles.writeButton}>글쓰기</Link>}
            </div>
        </div>
    );
}

export default BoardListView;