package com.spring.board.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.spring.board.dao.UserDao;
import com.spring.board.service.UserService;
import com.spring.board.vo.CodeVo; 
import com.spring.board.vo.UserVo;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    
    @Override
    public int userJoin(UserVo userVo) throws Exception {
        // JSP에서 xxx-xxx 형식으로 들어오는 우편번호를 xxxxxx 형식으로 변경
        if (StringUtils.hasText(userVo.getUserAddr1()) && userVo.getUserAddr1().contains("-")) {
            userVo.setUserAddr1(userVo.getUserAddr1().replace("-", ""));
        }
        
        // creator, modifier 설정 (예: 현재 로그인한 사용자 ID)
        // 여기서는 간단하게 가입하는 사용자의 ID를 creator로 설정합니다.
        userVo.setCreator(userVo.getUserId());

        return userDao.insertUser(userVo);
    }
    
    @Override
    public UserVo userLogin(UserVo userVo) throws Exception {
        return userDao.selectUser(userVo);
    }
    
    @Override
    public int idCheck(String userId) throws Exception {
        return userDao.idCheck(userId);
    }
    
    @Override
    public List<CodeVo> getPhoneCodeList() throws Exception {
        return userDao.getPhoneCodeList();
    }
}