package com.spring.board.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.board.dao.BoardDao;
import com.spring.board.vo.BoardVo;
import com.spring.board.vo.PageVo;
import com.spring.board.vo.CodeVo;

@Repository
public class BoardDaoImpl implements BoardDao{
	
	@Autowired
	private SqlSession sqlSession;
	
	@Override
	public String selectTest() throws Exception {
		// TODO Auto-generated method stub
		
		String a = sqlSession.selectOne("board.boardList");
		
		return a;
	}
	/**
	 * 
	 * */
	@Override
	public List<BoardVo> selectBoardList(PageVo pageVo) throws Exception {
		// TODO Auto-generated method stub
		return sqlSession.selectList("board.SelectBoardList", pageVo);
	}
	
    // [수정] 인터페이스에 맞춰 PageVo 파라미터를 받고, MyBatis에 전달
	@Override
	public int selectBoardCnt(PageVo pageVo) throws Exception {
		return sqlSession.selectOne("board.selectBoardCnt", pageVo);
	}
	
	@Override
	public BoardVo selectBoard(BoardVo boardVo) throws Exception {
		// TODO Auto-generated method stub
		return sqlSession.selectOne("board.boardView", boardVo);
	}
	
	@Override
	public int boardInsert(BoardVo boardVo) throws Exception {
		// TODO Auto-generated method stub
		return sqlSession.insert("board.boardInsert", boardVo);
	}
	
	@Override
	public int boardUpdate(BoardVo boardVo) throws Exception {
		// sqlSession의 update() 메소드를 사용하여 Mapper XML에 정의된 SQL을 실행합니다.
		// "board.boardUpdate"는 namespace가 'board'이고 id가 'boardUpdate'인 SQL 구문을 의미합니다.
		// 파라미터로 boardVo를 전달하여 SQL 문 내부에서 #{boardNo}, #{boardTitle} 등으로 사용할 수 있습니다.
		return sqlSession.update("board.boardUpdate", boardVo);
	}
	
	@Override
	public int boardDelete(BoardVo boardVo) throws Exception {
	    // sqlSession을 통해 mapper XML 파일에 정의된 쿼리를 실행합니다.
	    // "board.boardDelete"는 namespace가 "board"이고 id가 "boardDelete"인 쿼리를 의미합니다.
	    return sqlSession.delete("board.boardDelete", boardVo);
	}
	
    @Override // [추가]
    public List<CodeVo> selectBoardType() throws Exception {
        return sqlSession.selectList("board.selectBoardType");
    }
	
}
