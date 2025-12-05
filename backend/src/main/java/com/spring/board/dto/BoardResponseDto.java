package com.spring.board.dto;

import com.spring.board.vo.BoardVo;

/**
 * BoardResponseDto (Data Transfer Object)
 * - 역할: 서버가 클라이언트(JSP, Vue 등)에게 응답으로 보낼 게시글 정보를 담는 객체.
 * - 특징: 
 *   1. 원본 데이터(BoardVo)를 기반으로 생성.
 *   2. 비즈니스 로직(익명 처리, 소유자 여부 판단)을 포함하여 화면 표시에 최적화된 데이터를 구성.
 */
public class BoardResponseDto {

    // BoardVo의 필드들 (화면에 보여줄 정보)
    private String boardType;
    private int boardNum;
    private String boardTitle;
    private String boardComment;
    private String creator; // 익명 처리된 작성자 이름이 담길 필드
    private String boardTypeName;

    // 클라이언트의 권한 제어를 위한 추가 정보
    private boolean isOwner; // 이 게시글이 현재 요청을 보낸 사용자의 것인지 여부

    /**
     * 생성자
     * @param boardVo   DB에서 조회한 원본 게시글 객체
     * @param loginUserName 현재 로그인한 사용자의 이름 (로그인 안 했으면 null)
     */
    public BoardResponseDto(BoardVo boardVo, String loginUserName) {
        // 1. BoardVo의 데이터를 그대로 복사
        this.boardType = boardVo.getBoardType();
        this.boardNum = boardVo.getBoardNum();
        this.boardTitle = boardVo.getBoardTitle();
        this.boardComment = boardVo.getBoardComment();
        this.boardTypeName = boardVo.getBoardTypeName();

        // 2. [비즈니스 로직 1] 익명 게시판 타입인지 확인하고 작성자명 처리
        if ("a03".equals(boardVo.getBoardType())) { // 'a03'은 익명 게시판 타입 코드라고 가정
            this.creator = "익명";
        } else {
            this.creator = boardVo.getCreator();
        }

        // 3. [비즈니스 로직 2] 소유자 여부 판단
        this.isOwner = (loginUserName != null && loginUserName.equals(boardVo.getCreator()));
    }

    // --- 이하 모든 필드에 대한 Getter와 Setter ---

    public String getBoardType() {
        return boardType;
    }

    public void setBoardType(String boardType) {
        this.boardType = boardType;
    }

    public int getBoardNum() {
        return boardNum;
    }

    public void setBoardNum(int boardNum) {
        this.boardNum = boardNum;
    }

    public String getBoardTitle() {
        return boardTitle;
    }

    public void setBoardTitle(String boardTitle) {
        this.boardTitle = boardTitle;
    }

    public String getBoardComment() {
        return boardComment;
    }

    public void setBoardComment(String boardComment) {
        this.boardComment = boardComment;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getBoardTypeName() {
        return boardTypeName;
    }

    public void setBoardTypeName(String boardTypeName) {
        this.boardTypeName = boardTypeName;
    }

    // [핵심] boolean 타입의 표준 getter (주로 JSON 변환 시 사용)
    public boolean isOwner() {
        return isOwner;
    }
    
    /**
     * [JSP 호환용 Getter] 
     * JSP의 EL(${board.isOwner})이 자바빈 규약에 따라 'getIsOwner' 메소드를 찾기 때문에 추가.
     * @return 소유자 여부
     */
    public boolean getIsOwner() {
        return isOwner;
    }

    public void setOwner(boolean isOwner) {
        this.isOwner = isOwner;
    }
}