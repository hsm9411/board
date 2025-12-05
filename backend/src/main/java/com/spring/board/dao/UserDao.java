package com.spring.board.dao;

import java.util.List;
import com.spring.board.vo.CodeVo; // [수정]
import com.spring.board.vo.UserVo;

public interface UserDao {
    public int insertUser(UserVo userVo) throws Exception; // 회원가입
    public UserVo selectUser(UserVo userVo) throws Exception; // 로그인
    public int idCheck(String userId) throws Exception;   // 아이디 중복체크
    public List<CodeVo> getPhoneCodeList() throws Exception;
}