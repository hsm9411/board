package com.spring.mbti.vo;

/**
 * 클라이언트(브라우저)에서 서버로 사용자의 개별 답변을 전송할 때 사용하는 VO입니다.
 * 결과 계산에 필요한 boardType과 answerValue만 포함합니다.
 */
public class MbtiAnswerVo {

    private String boardType;  // 답변한 질문의 유형 (e.g., "EI")
    private int answerValue;   // 사용자가 선택한 값 (1~7)

    // Jackson 라이브러리가 JSON 데이터를 이 VO 객체로 변환하기 위해
    // 매개변수가 없는 기본 생성자가 반드시 필요합니다.
    public MbtiAnswerVo() {
    }

    // --- Getters and Setters ---
    public String getBoardType() {
        return boardType;
    }
    public void setBoardType(String boardType) {
        this.boardType = boardType;
    }
    public int getAnswerValue() {
        return answerValue;
    }
    public void setAnswerValue(int answerValue) {
        this.answerValue = answerValue;
    }
}