package com.spring.board.service;

import java.util.List;
import com.spring.board.vo.CodeVo;
import com.spring.board.vo.UserVo;

public interface UserService {
    public int userJoin(UserVo userVo) throws Exception; // 회원가입
    public UserVo userLogin(UserVo userVo) throws Exception; // 로그인
    public int idCheck(String userId) throws Exception;   // 아이디 중복체크
    public List<CodeVo> getPhoneCodeList() throws Exception;
}