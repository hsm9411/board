package com.spring.board.controller;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;

import com.spring.board.service.UserService;
import com.spring.board.vo.CodeVo;
import com.spring.board.vo.UserVo;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    //================================================================================
    // 화면(View) 이동 관련 메소드들
    // - 주로 페이지를 반환하는 역할을 담당합니다.
    //================================================================================

    /**
     * [GET] /user/signup.do
     * 기능: 회원가입 페이지로 이동합니다.
     * 특징: 전화번호 국번 목록(phoneCodeList)을 조회하여 모델에 담아 JSP로 전달합니다.
     */
    @RequestMapping(value = "/user/signup.do", method = RequestMethod.GET)
    public String signup(Model model) throws Exception {
        List<CodeVo> phoneCodeList = userService.getPhoneCodeList();
        model.addAttribute("phoneCodeList", phoneCodeList);
        return "user/signup";
    }

    /**
     * [GET] /user/login.do
     * 기능: 로그인 페이지로 이동합니다.
     */
    @RequestMapping(value = "/user/login.do", method = RequestMethod.GET)
    public String login() throws Exception {
        return "user/login";
    }
    
    //================================================================================
    // 기능(Action) 처리 관련 메소드들
    // - 데이터 처리 후 리다이렉트하거나, AJAX 요청에 대한 데이터를 반환합니다.
    //================================================================================
    
    /**
     * [POST] /user/loginAction.do (AJAX)
     * 기능: 사용자의 로그인 요청을 비동기 방식으로 처리합니다.
     * 특징: 아이디 존재 여부를 먼저 확인하여 상세한 오류 메시지를 반환합니다.
     */
    @RequestMapping(value = "/user/loginAction.do", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> loginAction(UserVo userVo, HttpSession session) throws Exception {
        Map<String, Object> response = new HashMap<>();

        // 1. 아이디 존재 여부 먼저 확인
        int idCount = userService.idCheck(userVo.getUserId());
        if (idCount == 0) {
            response.put("status", "error");
            response.put("message", "존재하지 않는 아이디입니다.");
            return response;
        }

        // 2. 아이디가 존재하면, 비밀번호 일치 여부 확인
        UserVo loginUser = userService.userLogin(userVo);
        if (loginUser == null) {
            // 아이디는 맞지만 비밀번호가 틀린 경우
            response.put("status", "error");
            response.put("message", "비밀번호가 일치하지 않습니다.");
            return response;
        }
        
        // 3. 로그인 성공: 세션에 사용자 정보 저장
        session.setAttribute("loginUser", loginUser);
        
        response.put("status", "success");
        response.put("redirectUrl", "/board/boardList.do"); // 성공 시 이동할 URL
        
        return response;
    }

    /**
     * [GET] /user/logout.do
     * 기능: 사용자의 로그아웃 요청을 처리합니다.
     * 특징: 세션을 무효화하고 게시판 목록으로 리다이렉트합니다.
     */
    @RequestMapping(value = "/user/logout.do", method = RequestMethod.GET)
    public String logout(HttpSession session) throws Exception {
        session.invalidate();
        return "redirect:/board/boardList.do";
    }

    //================================================================================
    // AJAX 요청 처리 API 메소드들
    // - @ResponseBody 어노테이션을 사용하여 JSON 형태의 데이터를 반환합니다.
    //================================================================================
    
    /**
     * [POST] /user/signupAction.do (AJAX)
     * 기능: 회원가입 요청을 비동기 방식으로 처리합니다.
     * 구조:
     *   1. 모든 유효성 검사를 수행하여 발생한 모든 오류를 'errors' Map에 수집합니다.
     *   2. 'errors' Map이 비어있지 않으면, { "status": "error", "errors": { ... } } 형태로 응답합니다.
     *   3. 'errors' Map이 비어있으면, 회원가입을 진행하고 성공 응답을 보냅니다.
     * 특징: 이 구조는 클라이언트가 어떤 항목들이 잘못되었는지 한 번에 알 수 있게 해줍니다.
     */
    @RequestMapping(value = "/user/signupAction.do", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> signupAction(UserVo userVo) throws Exception {
        
        Map<String, Object> response = new HashMap<>();
        Map<String, String> errors = new HashMap<>();

        // --- 서버단 유효성 검사 (바이트 길이 기준으로 수정) ---
        // 1. 아이디 검사 (영문/숫자이므로 byte와 length가 동일하지만, 일관성을 위해 getBytes 사용)
        if (userVo.getUserId() == null || userVo.getUserId().trim().isEmpty()) {
            errors.put("userId", "아이디는 필수 입력 항목입니다.");
        } else if (userVo.getUserId().getBytes(StandardCharsets.UTF_8).length > 15) { // [수정] 바이트 길이 검사
            errors.put("userId", "아이디의 길이가 너무 깁니다. (최대 15 bytes)");
        }

        // 2. 비밀번호 검사
        if (userVo.getUserPw() == null || userVo.getUserPw().trim().isEmpty()) {
            errors.put("userPw", "비밀번호는 필수 입력 항목입니다.");
        } else if (userVo.getUserPw().getBytes(StandardCharsets.UTF_8).length > 16) { // [수정] 바이트 길이 검사
            errors.put("userPw", "비밀번호의 길이가 너무 깁니다. (최대 16 bytes)");
        }

        // 3. 이름 검사 (한글/영문 혼용 가능성이 높으므로 바이트 검사가 특히 중요)
        if (userVo.getUserName() == null || userVo.getUserName().trim().isEmpty()) {
            errors.put("userName", "이름은 필수 입력 항목입니다.");
        } else if (userVo.getUserName().getBytes(StandardCharsets.UTF_8).length > 15) { // [수정] 바이트 길이 검사 (한글 5자 = 15 bytes)
            errors.put("userName", "이름의 길이가 너무 깁니다. (한글 5자, 영문 15자 이내)");
        }
        
        // 4. 전화번호 검사
        if (userVo.getUserPhone2() == null || userVo.getUserPhone2().trim().isEmpty() ||
            userVo.getUserPhone3() == null || userVo.getUserPhone3().trim().isEmpty()) {
            errors.put("userPhone", "전화번호를 모두 입력해주세요.");
        }
        
        // 5. 아이디 중복 검사
        if (!errors.containsKey("userId") && userService.idCheck(userVo.getUserId()) > 0) {
            errors.put("userId", "이미 사용 중인 아이디입니다.");
        }

        // 수집된 오류가 있으면 반환
        if (!errors.isEmpty()) {
            response.put("status", "error");
            response.put("errors", errors);
            return response;
        }

        // 모든 검사를 통과한 경우 회원가입
        userService.userJoin(userVo);
        
        response.put("status", "success");
        response.put("message", "회원가입이 성공적으로 완료되었습니다. 로그인해주세요.");
        response.put("redirectUrl", "/user/login.do");
        
        return response;
    }

    /**
     * [GET] /user/idCheck.do (AJAX)
     * 기능: 아이디 중복 여부를 확인합니다.
     * 반환: 1 (중복), 0 (사용 가능)
     */
    @RequestMapping(value = "/user/idCheck.do", method = RequestMethod.GET)
    @ResponseBody
    public int idCheck(@RequestParam("userId") String userId) throws Exception {
        return userService.idCheck(userId);
    }
    
	// ===================================================================
	// START: Vue.js를 위한 사용자 API
	// - 모든 URL은 '/api'로 시작하며, 순수 데이터(JSON)를 반환합니다.
	// ===================================================================
	
	/**
	 * [API] POST /api/users/login
	 * 기능    : Vue.js 클라이언트의 로그인 요청을 처리합니다.
	 * 요청 데이터 : userId, userPw
	 * 응답 데이터 : 
	 *   - 성공: { "status": "success", "user": { ...사용자 정보... } }
	 *   - 실패: { "status": "error", "message": "에러 메시지" }
	 */
	@RequestMapping(value = "/api/users/login", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> apiLoginAction(@RequestBody UserVo userVo, HttpSession session) throws Exception {
	    // Vue와 통신하기 위한 응답 데이터를 담을 Map 객체 생성
	    Map<String, Object> response = new HashMap<String, Object>();
	
	    // 1. [서비스 재사용] 기존에 구현된 idCheck 서비스를 사용하여 아이디 존재 여부 확인
	    int idCount = userService.idCheck(userVo.getUserId());
	    if (idCount == 0) {
	        // 아이디가 존재하지 않으면, 에러 상태와 메시지를 담아 반환
	        response.put("status", "error");
	        response.put("message", "존재하지 않는 아이디입니다.");
	        return response;
	    }
	
	    // 2. [서비스 재사용] 아이디가 존재하면, userLogin 서비스를 통해 비밀번호 일치 여부 확인
	    UserVo loginUser = userService.userLogin(userVo);
	    if (loginUser == null) {
	        // 비밀번호가 틀린 경우, 에러 상태와 메시지를 담아 반환
	        response.put("status", "error");
	        response.put("message", "비밀번호가 일치하지 않습니다.");
	        return response;
	    }
	    
	    // 3. [세션 처리] 로그인 성공 시, 세션에 사용자 정보를 저장하여 서버에 로그인 상태를 유지
	    session.setAttribute("loginUser", loginUser);
	    
	    // 4. [성공 응답] 성공 상태와 함께, 클라이언트에서 사용할 사용자 정보를 응답에 포함
	    response.put("status", "success");
	    response.put("user", loginUser);
	    
	    return response;
	}
	
	/**
	 * [API] POST /api/users/logout
	 * 기능    : Vue.js 클라이언트의 로그아웃 요청을 처리합니다.
	 * 응답 데이터 : { "status": "success" }
	 */
	@RequestMapping(value = "/api/users/logout", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> apiLogoutAction(HttpSession session) throws Exception {
	    // 세션을 무효화하여 서버의 로그인 기록을 삭제
	    session.invalidate();
	    
	    Map<String, Object> response = new HashMap<String, Object>();
	    response.put("status", "success");
	    return response;
	}
	
	/**
	 * [API] GET /api/users/session
	 * 기능    : 현재 로그인된 세션 정보를 확인합니다. 
	 *         (Vue 앱이 새로고침되거나 시작될 때, 로그인 상태를 복원하기 위해 사용)
	 * 응답 데이터 :
	 *   - 로그인 상태: { "status": "success", "user": { ...사용자 정보... } }
	 *   - 로그아웃 상태: { "status": "unauthorized" }
	 */
	@RequestMapping(value = "/api/users/session", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> apiGetSession(HttpSession session) throws Exception {
	    Map<String, Object> response = new HashMap<String, Object>();
	    
	    // 세션에서 "loginUser" 속성을 가져옵니다.
	    UserVo loginUser = (UserVo) session.getAttribute("loginUser");
	    
	    if (loginUser != null) {
	        // 세션이 존재하면, 성공 상태와 사용자 정보를 반환
	        response.put("status", "success");
	        response.put("user", loginUser);
	    } else {
	        // 세션이 없으면, '인증되지 않음' 상태를 반환
	        response.put("status", "unauthorized");
	    }
	    return response;
	}
	
	
    /**
	 * [API] POST /api/users/signup
	 * 기능    : Vue.js 클라이언트의 회원가입 요청을 처리합니다.
	 * --- [수정] ---
	 * UserVo를 직접 받는 대신 Map<String, String>으로 데이터를 받도록 변경합니다.
	 * 이 방식은 클라이언트가 보낸 JSON의 구조에 덜 민감하여, 400 Bad Request 오류를 피하는 데 매우 효과적입니다.
	 */
	@RequestMapping(value = "/api/users/signup", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> apiSignupAction(@RequestBody Map<String, String> signupData) throws Exception {
	    // 1. Map으로 받은 데이터를 UserVo 객체로 수동으로 옮겨 담습니다.
	    UserVo userVo = new UserVo();
	    userVo.setUserId(signupData.get("userId"));
	    userVo.setUserPw(signupData.get("userPw"));
	    userVo.setUserName(signupData.get("userName"));
	    userVo.setUserPhone1(signupData.get("userPhone1"));
	    userVo.setUserPhone2(signupData.get("userPhone2"));
	    userVo.setUserPhone3(signupData.get("userPhone3"));
	    userVo.setUserAddr1(signupData.get("userAddr1"));
	    userVo.setUserAddr2(signupData.get("userAddr2"));
	    userVo.setUserCompany(signupData.get("userCompany"));

	    // 2. 기존의 유효성 검사 및 회원가입 로직은 그대로 재사용합니다.
	    Map<String, Object> response = new HashMap<String, Object>();
	    Map<String, String> errors = new HashMap<String, String>();
	
	    // --- 서버단 유효성 검사 (기존 로직과 동일) ---
	    if (userVo.getUserId() == null || userVo.getUserId().trim().isEmpty()) {
	        errors.put("userId", "아이디는 필수 입력 항목입니다.");
	    } else if (userVo.getUserId().getBytes(StandardCharsets.UTF_8).length > 15) {
	        errors.put("userId", "아이디의 길이가 너무 깁니다. (최대 15 bytes)");
	    }
	    if (userVo.getUserPw() == null || userVo.getUserPw().trim().isEmpty()) {
	        errors.put("userPw", "비밀번호는 필수 입력 항목입니다.");
	    }
	    if (userVo.getUserName() == null || userVo.getUserName().trim().isEmpty()) {
	        errors.put("userName", "이름은 필수 입력 항목입니다.");
	    }
        // [추가] 전화번호 유효성 검사 추가
        if (userVo.getUserPhone2() == null || userVo.getUserPhone2().trim().isEmpty() ||
            userVo.getUserPhone3() == null || userVo.getUserPhone3().trim().isEmpty()) {
            errors.put("userPhone", "전화번호를 모두 입력해주세요.");
        }
	    // 아이디 필드에 다른 오류가 없을 때만 중복 검사를 수행
	    if (!errors.containsKey("userId") && userService.idCheck(userVo.getUserId()) > 0) {
	        errors.put("userId", "이미 사용 중인 아이디입니다.");
	    }
	    
	    // 3. [유효성 검증] 수집된 오류가 하나라도 있으면, 에러 상태와 오류 목록을 반환
	    if (!errors.isEmpty()) {
	        response.put("status", "error");
	        response.put("errors", errors);
	        return response;
	    }
	
	    // 4. [서비스 호출] 모든 검사를 통과하면, 회원가입 서비스 호출
	    userService.userJoin(userVo);
	    
	    // 5. [성공 응답] 성공 상태와 메시지를 담아 반환
	    response.put("status", "success");
	    response.put("message", "회원가입이 성공적으로 완료되었습니다. 로그인해주세요.");
	    
	    return response;
	}
	
	/**
	 * [API] GET /api/users/id-check
	 * 기능    : 회원가입 시 아이디 중복 여부를 실시간으로 확인합니다.
	 * 요청 데이터 : userId (쿼리 파라미터)
	 * 응답 데이터 : { "isDuplicate": true } 또는 { "isDuplicate": false }
	 */
	@RequestMapping(value = "/api/users/id-check", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Boolean> apiIdCheck(@RequestParam("userId") String userId) throws Exception {
	    // 기존 idCheck.do는 숫자(0 또는 1)를 반환했지만,
	    // API에서는 의미가 명확한 JSON 객체를 반환하는 것이 더 좋습니다.
	    int count = userService.idCheck(userId);
	    
	    Map<String, Boolean> response = new HashMap<String, Boolean>();
	    response.put("isDuplicate", count > 0); // count가 0보다 크면 true(중복), 아니면 false(사용 가능)
	    
	    return response;
	}
	
	
	/**
	 * [API] GET /api/common/codes
	 * 기능    : 공통 코드를 조회합니다. (예: 전화번호 국번 목록)
	 * 요청 데이터 : type (쿼리 파라미터, 예: 'phone')
	 * 응답 데이터 : [ { "codeId": "010", "codeName": "010" }, ... ]
	 */
	@RequestMapping(value = "/api/common/codes", method = RequestMethod.GET)
	@ResponseBody
	public List<CodeVo> apiGetCommonCodes(@RequestParam("type") String type) throws Exception {
	    if ("phone".equals(type)) {
	        // 기존에 구현된 전화번호 국번 조회 서비스를 그대로 재사용합니다.
	        return userService.getPhoneCodeList();
	    }
	    // 향후 다른 종류의 코드가 필요할 경우를 대비해 null을 반환하거나 예외 처리를 할 수 있습니다.
	    return null;
	}
	
	
	// ===================================================================
	// END: Vue.js를 위한 사용자 API
	// ===================================================================
    
    
}