package com.spring.mbti.vo;

/**
 * 사용자의 최종 MBTI 결과와 각 지표별 점수를 저장하는 Value Object 클래스입니다.
 * 이 객체는 Service 계층에서 계산되어 Controller를 통해 View(결과 페이지)로 전달됩니다.
 */
public class MbtiResultVo {

    /**
     * 최종적으로 결정된 MBTI 유형 (예: "ENFP")
     */
    private String mbtiType;

    /**
     * 외향(E) 지표 총점
     */
    private int eScore;

    /**
     * 내향(I) 지표 총점
     */
    private int iScore;

    /**
     * 감각(S) 지표 총점
     */
    private int sScore;

    /**
     * 직관(N) 지표 총점
     */
    private int nScore;

    /**
     * 감정(F) 지표 총점
     */
    private int fScore;

    /**
     * 사고(T) 지표 총점
     */
    private int tScore;

    /**
     * 판단(J) 지표 총점
     */
    private int jScore;

    /**
     * 인식(P) 지표 총점
     */
    private int pScore;


    // --- Getter and Setter --- //

    public String getMbtiType() {
        return mbtiType;
    }

    public void setMbtiType(String mbtiType) {
        this.mbtiType = mbtiType;
    }

    public int geteScore() {
        return eScore;
    }

    public void seteScore(int eScore) {
        this.eScore = eScore;
    }

    public int getiScore() {
        return iScore;
    }

    public void setiScore(int iScore) {
        this.iScore = iScore;
    }

    public int getsScore() {
        return sScore;
    }

    public void setsScore(int sScore) {
        this.sScore = sScore;
    }

    public int getnScore() {
        return nScore;
    }

    public void setnScore(int nScore) {
        this.nScore = nScore;
    }

    public int getfScore() {
        return fScore;
    }

    public void setfScore(int fScore) {
        this.fScore = fScore;
    }

    public int gettScore() {
        return tScore;
    }

    public void settScore(int tScore) {
        this.tScore = tScore;
    }

    public int getjScore() {
        return jScore;
    }

    public void setjScore(int jScore) {
        this.jScore = jScore;
    }

    public int getpScore() {
        return pScore;
    }

    public void setpScore(int pScore) {
        this.pScore = pScore;
    }

    /**
     * 객체의 상태를 쉽게 확인하기 위한 toString() 메소드 오버라이드
     */
    @Override
    public String toString() {
        return "MbtiResultVo{" +
                "mbtiType='" + mbtiType + '\'' +
                ", eScore=" + eScore +
                ", iScore=" + iScore +
                ", sScore=" + sScore +
                ", nScore=" + nScore +
                ", fScore=" + fScore +
                ", tScore=" + tScore +
                ", jScore=" + jScore +
                ", pScore=" + pScore +
                '}';
    }
}