package com.spring.mbti.util;

import java.util.Map;

public class MbtiCalculator {

    /**
     * [명세 1] 사용자가 선택한 라디오 버튼 값(1~7)을 명시된 점수로 변환합니다.
     *          (매우 동의: +3점, 동의: +2점, ..., 매우 비동의: -3점)
     */
    public static int convertScore(int radioValue) {
        return radioValue - 4;
    }

    /**
     * [명세 2] 각 지표별 총점을 기반으로 최종 MBTI 유형을 결정합니다.
     *          "합산 점수가 같거나 모두 0점인 경우 사전순으로 빠른 유형"을 선택하는 로직이 포함됩니다.
     */
    public static String determineMbti(Map<Character, Integer> scores) {
        StringBuilder mbti = new StringBuilder();

        // 비교 연산자 '>='를 사용하여 동점일 경우, 사전순으로 빠른 유형이 선택되도록 구현
        mbti.append(scores.get('E') >= scores.get('I') ? 'E' : 'I'); // E < I
        mbti.append(scores.get('N') >= scores.get('S') ? 'N' : 'S'); // N < S
        mbti.append(scores.get('F') >= scores.get('T') ? 'F' : 'T'); // F < T
        mbti.append(scores.get('J') >= scores.get('P') ? 'J' : 'P'); // J < P

        return mbti.toString();
    }
}