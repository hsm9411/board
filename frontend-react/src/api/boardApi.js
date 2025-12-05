import apiClient from './apiClient';

/**
 * 게시판 관련 모든 API 호출을 관리합니다.
 */

// 게시판 종류 목록 조회
export const fetchBoardTypes = async () => {
    const response = await apiClient.get('/api/board-types');
    return response.data;
};

// 게시글 목록 조회 (페이징 및 필터링 포함)
export const fetchBoardList = async (pageNo, selectedTypes) => {
    const params = new URLSearchParams();
    params.append('pageNo', pageNo);
    selectedTypes.forEach(type => params.append('boardTypes', type));
    
    const response = await apiClient.get(`/api/boards?${params.toString()}`);
    return response.data; // { boardList: [...], pageInfo: {...} } 객체를 반환
};

// 게시글 상세 정보 조회
export const fetchBoardDetail = async (boardType, boardNum) => {
    const response = await apiClient.get(`/api/boards/${boardType}/${boardNum}`);
    return response.data;
};

// 새 게시글 작성
export const writeBoard = async (boardData) => {
    const response = await apiClient.post('/api/boards', boardData);
    return response.data;
};

// 게시글 수정
export const updateBoard = async (boardType, boardNum, boardData) => {
    const response = await apiClient.post(`/api/boards/${boardType}/${boardNum}`, boardData);
    return response.data;
};

// 게시글 삭제
export const deleteBoard = async (boardType, boardNum) => {
    const response = await apiClient.post('/api/boards/delete', { boardType, boardNum });
    return response.data;
};