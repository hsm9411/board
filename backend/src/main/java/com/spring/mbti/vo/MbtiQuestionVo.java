package com.spring.mbti.vo;

/**
 * MBTI 질문 하나의 정보를 담는 Value Object 클래스입니다.
 * DB의 BOARD 테이블 레코드와 1:1로 매핑됩니다.
 * boardNum은 이 로직에서 필요 없으므로 제거되었습니다.
 */
public class MbtiQuestionVo {

    /**
     * 문항 유형 (예: "EI", "IE", "NS", "SN"...)
     * DB 컬럼명: BOARD_TYPE
     */
    private String boardType;

    /**
     * 실제 질문 내용
     * DB 컬럼명: BOARD_COMMENT
     */
    private String boardComment;

    // --- Getter and Setter --- //

    public String getBoardType() {
        return boardType;
    }

    public void setBoardType(String boardType) {
        this.boardType = boardType;
    }

    public String getBoardComment() {
        return boardComment;
    }

    public void setBoardComment(String boardComment) {
        this.boardComment = boardComment;
    }

    @Override
    public String toString() {
        return "MbtiQuestionVo{" +
                "boardType='" + boardType + '\'' +
                ", boardComment='" + boardComment + '\'' +
                '}';
    }
}