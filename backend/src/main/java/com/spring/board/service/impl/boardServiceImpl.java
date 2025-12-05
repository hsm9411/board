package com.spring.board.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.board.dao.BoardDao;
import com.spring.board.service.boardService;
import com.spring.board.vo.BoardVo;
import com.spring.board.vo.PageVo;
import com.spring.board.vo.CodeVo;

@Service
public class boardServiceImpl implements boardService{
	
	@Autowired
	BoardDao boardDao;
	
	@Override
	public String selectTest() throws Exception {
		return boardDao.selectTest();
	}
	
	/**
	 * [수정] 서비스 계층의 '익명' 처리 로직을 제거합니다.
	 * 이제 이 메소드는 DB에서 조회한 '원본' 데이터를 가공 없이 그대로 반환하는 역할만 합니다.
	 * 모든 비즈니스 로직 처리는 컨트롤러 또는 DTO 계층에서 담당합니다.
	 */
	@Override
	public List<BoardVo> SelectBoardList(PageVo pageVo) throws Exception {
		// DAO를 통해 조회한 원본 목록을 즉시 반환
		return boardDao.selectBoardList(pageVo);
	}
	
    @Override
    public int selectBoardCnt(PageVo pageVo) throws Exception {
        return boardDao.selectBoardCnt(pageVo);
    }
	
	/**
	 * [수정] 서비스 계층의 '익명' 처리 로직을 제거합니다.
	 * DB에서 조회한 원본 BoardVo 객체를 그대로 반환합니다.
	 */
	@Override
	public BoardVo selectBoard(String boardType, int boardNum) throws Exception {
		BoardVo boardVo = new BoardVo();
		
		boardVo.setBoardType(boardType);
		boardVo.setBoardNum(boardNum);
		
		// DAO를 통해 조회한 원본 객체를 즉시 반환
		return boardDao.selectBoard(boardVo);
	}
	
	@Override
	public int boardInsert(BoardVo boardVo) throws Exception {
		return boardDao.boardInsert(boardVo);
	}
	
	@Override
	public int boardUpdate(BoardVo boardVo) throws Exception {
		return boardDao.boardUpdate(boardVo);
	}
	
	@Override
	public int boardDelete(BoardVo boardVo) throws Exception {
	    return boardDao.boardDelete(boardVo);
	}
	
    @Override
    public List<CodeVo> selectBoardType() throws Exception {
        return boardDao.selectBoardType();
    }
	
}