package com.spring.mbti.service.impl;

import com.spring.mbti.dao.MbtiDao;
import com.spring.mbti.service.MbtiService;
import com.spring.mbti.util.MbtiCalculator;
// MbtiAnswerVo는 더 이상 사용하지 않으므로 import 제거 가능
// import com.spring.mbti.vo.MbtiAnswerVo;
import com.spring.mbti.vo.MbtiQuestionVo;
import com.spring.mbti.vo.MbtiResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MbtiServiceImpl implements MbtiService {

    @Autowired
    private MbtiDao mbtiDao;

    @Override
    public List<MbtiQuestionVo> getMbtiTestSet(int questionsPerDimension) throws Exception {
        List<MbtiQuestionVo> finalTestSet = new ArrayList<>();
        String[] dimensions = {"EI", "SN", "TF", "JP"};
        for (String dim : dimensions) {
            Map<String, Object> params = new HashMap<>();
            params.put("typeA", dim.substring(0, 1));
            params.put("typeB", dim.substring(1, 2));
            params.put("limit", questionsPerDimension);
            finalTestSet.addAll(mbtiDao.getRandomQuestionsByType(params));
        }
        Collections.shuffle(finalTestSet);
        return finalTestSet;
    }

    /**
     * [수정] MbtiAnswerVo 대신 LinkedHashMap 리스트를 직접 처리하도록 로직을 변경합니다.
     */
    @Override
    public MbtiResultVo calculateResult(List<LinkedHashMap<String, Object>> answers) throws Exception {
        Map<Character, Integer> scores = new HashMap<>();
        char[] types = {'E', 'I', 'S', 'N', 'T', 'F', 'J', 'P'};

        for (char type : types) {
            scores.put(type, 0);
        }

        // LinkedHashMap 리스트를 순회하며 점수를 계산합니다.
        for (LinkedHashMap<String, Object> answerMap : answers) {
            // Map에서 key를 이용해 직접 값을 꺼냅니다.
            String boardType = (String) answerMap.get("boardType");
            // JSON에서 숫자는 보통 Integer로 변환되므로 Integer로 형 변환합니다.
            int radioValue = (Integer) answerMap.get("answerValue");
            
            if (boardType == null || boardType.length() != 2) continue;

            int score = MbtiCalculator.convertScore(radioValue);
            char firstType = boardType.charAt(0);
            char secondType = boardType.charAt(1);

            if (score > 0) {
                scores.put(firstType, scores.get(firstType) + score);
            } else if (score < 0) {
                scores.put(secondType, scores.get(secondType) + Math.abs(score));
            }
        }

        String finalMbtiType = MbtiCalculator.determineMbti(scores);
        MbtiResultVo result = new MbtiResultVo();
        result.setMbtiType(finalMbtiType);

        // 백분율 계산 로직은 동일합니다.
        int totalEI = scores.get('E') + scores.get('I');
        result.seteScore(totalEI > 0 ? (int) Math.round((double) scores.get('E') / totalEI * 100) : 50);
        result.setiScore(100 - result.geteScore());

        int totalSN = scores.get('S') + scores.get('N');
        result.setsScore(totalSN > 0 ? (int) Math.round((double) scores.get('S') / totalSN * 100) : 50);
        result.setnScore(100 - result.getsScore());

        int totalTF = scores.get('T') + scores.get('F');
        result.settScore(totalTF > 0 ? (int) Math.round((double) scores.get('T') / totalTF * 100) : 50);
        result.setfScore(100 - result.gettScore());

        int totalJP = scores.get('J') + scores.get('P');
        result.setjScore(totalJP > 0 ? (int) Math.round((double) scores.get('J') / totalJP * 100) : 50);
        result.setpScore(100 - result.getjScore());

        return result;
    }
}