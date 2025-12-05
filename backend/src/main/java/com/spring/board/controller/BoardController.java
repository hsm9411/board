package com.spring.board.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.board.HomeController;
import com.spring.board.dto.BoardResponseDto; // [신규] DTO 임포트
import com.spring.board.service.boardService;
import com.spring.board.vo.BoardVo;
import com.spring.board.vo.PageVo;
import com.spring.board.vo.UserVo;
import com.spring.board.vo.CodeVo;
import com.spring.common.CommonUtil;

@Controller
public class BoardController {
	
	@Autowired 
	boardService boardService;
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	// ===================================================================
	// START: JSP용 메소드들 (DTO 적용으로 수정됨)
	// ===================================================================
	
	@RequestMapping(value = "/board/boardList.do", method = RequestMethod.GET)
	public String boardList(Locale locale, Model model, HttpSession session, @RequestParam(value="pageNo", required=false, defaultValue="1") int pageNo, @RequestParam(value="boardTypes", required=false) List<String> boardTypes) throws Exception {
	    // 1. 데이터 조회
        List<CodeVo> boardTypeList = boardService.selectBoardType();
	    if (boardTypes == null || boardTypes.isEmpty()) {
            boardTypes = new ArrayList<>();
            for (CodeVo code : boardTypeList) {
                boardTypes.add(code.getCodeId());
            }
        }
	    PageVo pageVo = new PageVo();
	    pageVo.setPageNo(pageNo);
	    pageVo.setBoardTypes(boardTypes);
	    int totalCnt = boardService.selectBoardCnt(pageVo);
	    pageVo.setTotalCnt(totalCnt);
	    List<BoardVo> boardVoList = boardService.SelectBoardList(pageVo);
        
        // 2. DTO 변환
        UserVo loginUser = (UserVo) session.getAttribute("loginUser");
        String loginUserName = (loginUser != null) ? loginUser.getUserName() : null;
        
        List<BoardResponseDto> boardDtoList = new ArrayList<>();
        for (BoardVo boardVo : boardVoList) {
            boardDtoList.add(new BoardResponseDto(boardVo, loginUserName));
        }

	    // 3. Model에 DTO 데이터 추가
	    model.addAttribute("boardTypeList", boardTypeList);
	    model.addAttribute("boardList", boardDtoList); // DTO 리스트 전달
	    model.addAttribute("pageVo", pageVo);
	    return "board/boardList";
	}
	
	@RequestMapping(value = "/board/{boardType}/{boardNum}/boardView.do", method = RequestMethod.GET)
	public String boardView(Locale locale, Model model, HttpSession session, @PathVariable("boardType")String boardType, @PathVariable("boardNum")int boardNum) throws Exception {
		// 1. 원본 데이터 조회
        BoardVo boardVo = boardService.selectBoard(boardType, boardNum);
		
        // 2. 로그인 사용자 정보 조회
        UserVo loginUser = (UserVo) session.getAttribute("loginUser");
        String loginUserName = (loginUser != null) ? loginUser.getUserName() : null;

        // 3. DTO로 변환
        BoardResponseDto boardDto = new BoardResponseDto(boardVo, loginUserName);

		// 4. Model에 DTO 전달
        model.addAttribute("board", boardDto);
		return "board/boardView";
	}
	
	@RequestMapping(value = "/board/boardWrite.do", method = RequestMethod.GET)
	public String boardWrite(HttpSession session, Model model) throws Exception {
	    if (session.getAttribute("loginUser") == null) { return "redirect:/user/login.do"; }
	    List<CodeVo> boardTypeList = boardService.selectBoardType();
	    model.addAttribute("boardTypeList", boardTypeList);
	    return "board/boardWrite";
	}
	
	@RequestMapping(value = "/board/boardWriteAction.do", method = RequestMethod.POST)
	@ResponseBody
	public String boardWriteAction(BoardVo boardVo, HttpSession session) throws Exception {
	    HashMap<String, String> result = new HashMap<>(); CommonUtil commonUtil = new CommonUtil();
	    UserVo loginUser = (UserVo) session.getAttribute("loginUser");
	    if (loginUser == null) { result.put("success", "N"); result.put("message", "로그인이 필요합니다."); return commonUtil.getJsonCallBackString(" ", result); }
	    boardVo.setCreator(loginUser.getUserName());
	    int resultCnt = boardService.boardInsert(boardVo);
	    result.put("success", (resultCnt > 0) ? "Y" : "N");
	    return commonUtil.getJsonCallBackString(" ", result);
	}
	
	@RequestMapping(value = "/board/{boardType}/{boardNum}/boardEdit.do", method = RequestMethod.GET)
	public String boardEdit(Model model, HttpSession session, @PathVariable("boardType") String boardType, @PathVariable("boardNum") int boardNum, RedirectAttributes redirectAttributes) throws Exception {
	    BoardVo boardVo = boardService.selectBoard(boardType, boardNum);
	    UserVo loginUser = (UserVo) session.getAttribute("loginUser");
	    if (loginUser == null || !loginUser.getUserName().equals(boardVo.getCreator())) {
	    	redirectAttributes.addFlashAttribute("error", "수정 권한이 없습니다.");
	        return "redirect:/board/boardList.do";
	    }
	    model.addAttribute("board", boardVo);
	    return "board/boardEdit";
	}

	@RequestMapping(value = "/board/boardUpdateAction.do", method = RequestMethod.POST)
	@ResponseBody
	public String boardUpdateAction(BoardVo boardVo, HttpSession session) throws Exception {
		HashMap<String, String> result = new HashMap<>(); CommonUtil commonUtil = new CommonUtil();
		UserVo loginUser = (UserVo) session.getAttribute("loginUser");
		BoardVo originalBoard = boardService.selectBoard(boardVo.getBoardType(), boardVo.getBoardNum());
		if (loginUser == null || !loginUser.getUserName().equals(originalBoard.getCreator())) { result.put("success", "N"); result.put("message", "수정 권한이 없습니다."); return commonUtil.getJsonCallBackString(" ", result); }
		boardVo.setModifier(loginUser.getUserName());
		int resultCnt = boardService.boardUpdate(boardVo);
		result.put("success", (resultCnt > 0) ? "Y" : "N");
		return commonUtil.getJsonCallBackString(" ", result);
	}
	
	@RequestMapping(value = "/board/boardDelete.do", method = RequestMethod.POST)
	public String boardDeleteAction(BoardVo boardVo, HttpSession session, RedirectAttributes redirectAttributes) throws Exception {
		BoardVo originalBoard = boardService.selectBoard(boardVo.getBoardType(), boardVo.getBoardNum());
		UserVo loginUser = (UserVo) session.getAttribute("loginUser");
		if (loginUser == null || !loginUser.getUserName().equals(originalBoard.getCreator())) { redirectAttributes.addFlashAttribute("error", "삭제 권한이 없습니다."); return "redirect:/board/boardList.do"; }
	    boardService.boardDelete(boardVo);
	    return "redirect:/board/boardList.do";
	}

	// ===================================================================
	// START: Vue.js를 위한 게시판 API (DTO 적용으로 수정됨)
	// ===================================================================
	
	@RequestMapping(value = "/api/board-types", method = RequestMethod.GET)
	@ResponseBody
	public List<CodeVo> apiGetBoardTypes() throws Exception { return boardService.selectBoardType(); }

	@RequestMapping(value = "/api/boards", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> apiGetBoardList(@RequestParam(value="pageNo", required=false, defaultValue="1") int pageNo, @RequestParam(value="boardTypes", required=false) List<String> boardTypes, HttpSession session) throws Exception {
	    PageVo pageVo = new PageVo(); pageVo.setPageNo(pageNo);
	    if (boardTypes == null || boardTypes.isEmpty()) { List<CodeVo> allBoardTypes = boardService.selectBoardType(); boardTypes = new ArrayList<String>(); for (CodeVo code : allBoardTypes) { boardTypes.add(code.getCodeId()); } }
	    pageVo.setBoardTypes(boardTypes);
	    int totalCnt = boardService.selectBoardCnt(pageVo); pageVo.setTotalCnt(totalCnt);
	    List<BoardVo> boardVoList = boardService.SelectBoardList(pageVo);
	    UserVo loginUser = (UserVo) session.getAttribute("loginUser"); String loginUserName = (loginUser != null) ? loginUser.getUserName() : null;
	    List<BoardResponseDto> boardDtoList = new ArrayList<>();
	    for (BoardVo boardVo : boardVoList) { boardDtoList.add(new BoardResponseDto(boardVo, loginUserName)); }
	    Map<String, Object> response = new HashMap<>();
	    response.put("boardList", boardDtoList);
	    response.put("pageInfo", pageVo);
	    return response;
	}

	@RequestMapping(value = "/api/boards/{boardType}/{boardNum}", method = RequestMethod.GET)
	@ResponseBody
	public BoardResponseDto apiGetBoardView(@PathVariable("boardType") String boardType, @PathVariable("boardNum") int boardNum, HttpSession session) throws Exception {
	    BoardVo boardVo = boardService.selectBoard(boardType, boardNum);
	    if (boardVo == null) { return null; }
	    UserVo loginUser = (UserVo) session.getAttribute("loginUser");
	    String loginUserName = (loginUser != null) ? loginUser.getUserName() : null;
	    BoardResponseDto responseDto = new BoardResponseDto(boardVo, loginUserName);
	    return responseDto;
	}

	@RequestMapping(value = "/api/boards", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> apiBoardWrite(@RequestBody BoardVo boardVo, HttpSession session) throws Exception {
	    Map<String, Object> response = new HashMap<>(); UserVo loginUser = (UserVo) session.getAttribute("loginUser");
	    if (loginUser == null) { response.put("status", "error"); response.put("message", "로그인이 필요합니다."); return response; }
	    boardVo.setCreator(loginUser.getUserName()); int resultCnt = boardService.boardInsert(boardVo);
	    response.put("status", (resultCnt > 0) ? "success" : "fail");
	    return response;
	}

	@RequestMapping(value = "/api/boards/{boardType}/{boardNum}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> apiBoardUpdate(@RequestBody BoardVo boardVo, HttpSession session, @PathVariable("boardType") String boardType, @PathVariable("boardNum") int boardNum) throws Exception {
	    Map<String, Object> response = new HashMap<>(); UserVo loginUser = (UserVo) session.getAttribute("loginUser");
	    BoardVo originalBoard = boardService.selectBoard(boardType, boardNum);
	    if (loginUser == null || !loginUser.getUserName().equals(originalBoard.getCreator())) { response.put("status", "error"); response.put("message", "수정 권한이 없습니다."); return response; }
	    boardVo.setBoardNum(boardNum); boardVo.setBoardType(boardType); boardVo.setModifier(loginUser.getUserName());
	    int resultCnt = boardService.boardUpdate(boardVo);
	    response.put("status", (resultCnt > 0) ? "success" : "fail");
	    return response;
	}

	@RequestMapping(value = "/api/boards/delete", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> apiBoardDelete(@RequestBody BoardVo boardVo, HttpSession session) throws Exception {
	    Map<String, Object> response = new HashMap<>(); UserVo loginUser = (UserVo) session.getAttribute("loginUser");
	    BoardVo originalBoard = boardService.selectBoard(boardVo.getBoardType(), boardVo.getBoardNum());
	    if (loginUser == null || !loginUser.getUserName().equals(originalBoard.getCreator())) { response.put("status", "error"); response.put("message", "삭제 권한이 없습니다."); return response; }
	    int resultCnt = boardService.boardDelete(boardVo);
	    response.put("status", (resultCnt > 0) ? "success" : "fail");
	    return response;
	}
}