package com.spring.mbti.controller;

import com.spring.mbti.service.MbtiService;
import com.spring.mbti.vo.MbtiQuestionVo;
import com.spring.mbti.vo.MbtiResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedHashMap;
import java.util.List;

@Controller
public class MbtiController {

    private static final Logger logger = LoggerFactory.getLogger(MbtiController.class);
    private static final int QUESTIONS_PER_DIMENSION = 5;

    @Autowired
    private MbtiService mbtiService;

    // ===================================================================
    // START: [신규] Vue.js 클라이언트를 위한 전용 API
    // - 모든 경로는 "/api/mbti" 로 시작합니다.
    // - 순수 JSON 데이터만 반환합니다.
    // ===================================================================

    @RequestMapping(value = "/api/mbti/questions", method = RequestMethod.GET)
    @ResponseBody
    public List<MbtiQuestionVo> apiGetMbtiQuestions() throws Exception {
        logger.info("Vue API 요청: /api/mbti/questions");
        return mbtiService.getMbtiTestSet(QUESTIONS_PER_DIMENSION);
    }

    @RequestMapping(value = "/api/mbti/result", method = RequestMethod.POST)
    @ResponseBody
    public MbtiResultVo apiGetMbtiResult(@RequestBody List<LinkedHashMap<String, Object>> rawAnswers) throws Exception {
        logger.info("Vue API 요청: /api/mbti/result, 답변 개수: {}", rawAnswers.size());
        return mbtiService.calculateResult(rawAnswers);
    }

    // ===================================================================
    // START: [기존] JSP 페이지 및 AJAX을 위한 메소드들
    // ===================================================================

    @RequestMapping(value = "/mbti/test.do", method = RequestMethod.GET)
    public String showTestPage() {
        return "mbti/test";
    }

    @RequestMapping(value = "/mbti/api/questions.do", method = RequestMethod.GET)
    @ResponseBody
    public List<MbtiQuestionVo> getMbtiQuestions() throws Exception {
        logger.info("JSP API 요청: /mbti/api/questions.do");
        return mbtiService.getMbtiTestSet(QUESTIONS_PER_DIMENSION);
    }
    
    @RequestMapping(value = "/mbti/api/result.do", method = RequestMethod.POST)
    @ResponseBody
    public MbtiResultVo getMbtiResult(@RequestBody List<LinkedHashMap<String, Object>> rawAnswers) throws Exception {
        logger.info("JSP API 요청: /mbti/api/result.do, 답변 개수: {}", rawAnswers.size());
        return mbtiService.calculateResult(rawAnswers);
    }
}