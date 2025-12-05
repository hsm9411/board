package com.spring.board.dao.impl;

import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.board.dao.UserDao;
import com.spring.board.vo.CodeVo;
import com.spring.board.vo.UserVo;

@Repository
public class UserDaoImpl implements UserDao {

    @Autowired
    private SqlSession sqlSession;

    @Override
    public int insertUser(UserVo userVo) throws Exception {
        // userMapper.xml의 namespace="user", id="insertUser"인 쿼리를 실행
        return sqlSession.insert("user.insertUser", userVo);
    }
    
    @Override
    public UserVo selectUser(UserVo userVo) throws Exception {
        return sqlSession.selectOne("user.selectUser", userVo);
    }
    
    @Override
    public int idCheck(String userId) throws Exception {
        return sqlSession.selectOne("user.idCheck", userId);
    }
    
    @Override
    public List<CodeVo> getPhoneCodeList() throws Exception {
        return sqlSession.selectList("user.getPhoneCodeList");
    }
}