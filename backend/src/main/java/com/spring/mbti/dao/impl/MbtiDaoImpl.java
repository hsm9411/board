package com.spring.mbti.dao.impl;

import com.spring.mbti.dao.MbtiDao;
import com.spring.mbti.vo.MbtiQuestionVo;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class MbtiDaoImpl implements MbtiDao {

    @Autowired
    private SqlSession sqlSession;

    private static final String NAMESPACE = "mbti.";

    /**
     * {@inheritDoc}
     * MyBatis의 selectList 메소드를 호출하여 'mbti.getRandomQuestionsByType' 쿼리를 실행합니다.
     * 파라미터로 받은 Map(typeA, typeB, limit)을 쿼리에 전달합니다.
     */
    @Override
    public List<MbtiQuestionVo> getRandomQuestionsByType(Map<String, Object> params) throws Exception {
        return sqlSession.selectList(NAMESPACE + "getRandomQuestionsByType", params);
    }
}