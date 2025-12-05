package com.spring.mbti.dao;

import com.spring.mbti.vo.MbtiQuestionVo;
import java.util.List;
import java.util.Map;

public interface MbtiDao {
    /**
     * 지정된 유형(예: EI/IE)에 해당하는 질문들을 DB에서 랜덤으로 N개 가져옵니다.
     * @param params 'typeA', 'typeB', 'limit' 키를 포함하는 Map
     * @return 랜덤 질문 VO 리스트
     * @throws Exception
     */
    List<MbtiQuestionVo> getRandomQuestionsByType(Map<String, Object> params) throws Exception;
}