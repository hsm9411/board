package com.spring.board.vo;

import java.util.List;

public class PageVo {
    private int pageNo;
    private int pageSize = 10;
    private int blockSize = 5; // 화면에 보여줄 페이지 번호 개수

    private int totalCnt;
    private int totalPage;

    private int startPage;     // 화면에 보일 시작 페이지 번호
    private int endPage;       // 화면에 보일 끝 페이지 번호
    private boolean prev;      // '이전' 버튼 활성화 여부
    private boolean next;      // '다음' 버튼 활성화 여부

    private int startRow;
    private int endRow;

    private List<String> boardTypes;

    /**
     * 총 게시글 수가 세팅될 때, 모든 페이징 관련 값을 계산하는 핵심 메소드
     * @param totalCnt DB에서 조회한 총 게시글 수
     */
    public void setTotalCnt(int totalCnt) {
        this.totalCnt = totalCnt;

        // 1. 총 페이지 수 계산
        this.totalPage = (int) Math.ceil((double) totalCnt / pageSize);
        
        // 게시글이 하나도 없을 경우에도 페이지는 1로 표시
        if (this.totalPage == 0) {
            this.totalPage = 1;
        }

        // =================================================================
        //            ✅ 새로운 '현재 페이지 중심' 페이징 로직
        // =================================================================
        
        // 2. 화면에 보일 페이지 번호(startPage, endPage) 계산
        if (this.totalPage <= this.blockSize) {
            // 규칙 2: 전체 페이지 수가 blockSize보다 작거나 같으면, 처음부터 끝까지 모든 페이지 번호를 보여준다.
            this.startPage = 1;
            this.endPage = this.totalPage;
        
        } else {
            // 규칙 4 (기본): 현재 페이지를 중앙에 둔다.
            int halfBlock = this.blockSize / 2;
            this.startPage = this.pageNo - halfBlock;
            this.endPage = this.pageNo + halfBlock;

            // 규칙 3: 시작 페이지가 1보다 작아질 경우 (페이지 목록이 앞부분일 때)
            if (this.startPage < 1) {
                this.startPage = 1;
                this.endPage = this.blockSize;
            }

            // 규칙 5: 끝 페이지가 전체 페이지 수를 넘어갈 경우 (페이지 목록이 뒷부분일 때)
            else if (this.endPage > this.totalPage) {
                this.endPage = this.totalPage;
                this.startPage = this.totalPage - this.blockSize + 1;
            }
        }

        // 3. '이전'/'다음' 버튼 표시 여부 계산 (규칙 6)
        // '이전' 버튼: startPage가 1보다 클 때만 표시
        this.prev = this.startPage > 1;
        // '다음' 버튼: endPage가 전체 페이지 수보다 작을 때만 표시
        this.next = this.endPage < this.totalPage;
        
        // 4. DB 조회를 위한 rownum 계산 (기존 로직과 동일)
        this.endRow = this.pageNo * this.pageSize;
        this.startRow = this.endRow - this.pageSize + 1;
    }

    // --- 이하 Getter and Setter (기존과 동일) ---
	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getBlockSize() {
		return blockSize;
	}

	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
	}

	public int getTotalCnt() {
		return totalCnt;
	}
    
	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getStartPage() {
		return startPage;
	}

	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}

	public int getEndPage() {
		return endPage;
	}

	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}

	public boolean isPrev() {
		return prev;
	}

	public void setPrev(boolean prev) {
		this.prev = prev;
	}

	public boolean isNext() {
		return next;
	}

	public void setNext(boolean next) {
		this.next = next;
	}

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	public int getEndRow() {
		return endRow;
	}

	public void setEndRow(int endRow) {
		this.endRow = endRow;
	}
	
    public List<String> getBoardTypes() {
        return boardTypes;
    }
    public void setBoardTypes(List<String> boardTypes) {
        this.boardTypes = boardTypes;
    }
}