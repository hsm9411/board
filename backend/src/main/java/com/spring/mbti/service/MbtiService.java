package com.spring.mbti.service;

import com.spring.mbti.vo.MbtiQuestionVo;
import com.spring.mbti.vo.MbtiResultVo;

import java.util.LinkedHashMap; // LinkedHashMap을 import
import java.util.List;

public interface MbtiService {

    List<MbtiQuestionVo> getMbtiTestSet(int questionsPerDimension) throws Exception;

    // [수정] MbtiAnswerVo 대신 LinkedHashMap 리스트를 받도록 시그니처를 변경합니다.
    MbtiResultVo calculateResult(List<LinkedHashMap<String, Object>> answers) throws Exception;

}