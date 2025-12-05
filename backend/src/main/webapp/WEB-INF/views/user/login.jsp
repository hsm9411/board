<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@include file="/WEB-INF/views/common/common.jsp"%>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>로그인</title>
<style>
    /* 회원가입 페이지와 유사한 오류 메시지 스타일 추가 */
    .error-message {
        color: red;
        font-size: 12px;
        height: 14px; /* 메시지 공간 확보 */
        display: block;
        padding-top: 10px;
    }
</style>
</head>
<script type="text/javascript">
	$j(document).ready(function(){
		
		// '로그인' 버튼 클릭 이벤트
		$j("#loginBtn").on("click", function(){
			var userId = $j("input[name='userId']").val();
			var userPw = $j("input[name='userPw']").val();
			var messageSpan = $j("#loginMessage");

			messageSpan.text(""); // 이전 오류 메시지 초기화

			// 1. 클라이언트 단 유효성 검사
			if (userId.trim() === "") {
				messageSpan.text("아이디를 입력해주세요.");
				return;
			}
			if (userPw.trim() === "") {
				messageSpan.text("비밀번호를 입력해주세요.");
				return;
			}
			
			// 2. form 데이터를 직렬화하여 AJAX로 전송
			var param = $j("#loginForm").serialize();
			
			$j.ajax({
				url: "/user/loginAction.do",
				type: "POST",
				data: param,
				dataType: "json",
				success: function(response) {
					if (response.status === 'success') {
						// 로그인 성공 시, 서버가 알려준 URL로 페이지 이동
						location.href = response.redirectUrl;
					} else {
						// 로그인 실패 시, 서버가 보낸 오류 메시지를 화면에 표시
						messageSpan.text(response.message);
					}
				},
				error: function() {
					alert("로그인 처리 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
				}
			});
		});

        // 비밀번호 입력창에서 Enter 키를 누르면 로그인 버튼 클릭 효과
        $j("input[name='userPw']").on("keypress", function(e) {
            if (e.which == 13) { // 13 is the keycode for Enter
                $j("#loginBtn").click();
            }
        });
	});
</script>
<body>

<!-- [수정] form 태그에 id 부여, action/method 제거 -->
<form id="loginForm">
	<table align="center">
		<tr>
			<td colspan="2" align="center">
				<h2>로그인</h2>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<table border="1">
					<tr>
						<td width="120" align="center">아이디</td>
						<td width="300"><input type="text" name="userId" style="width: 95%;"></td>
					</tr>
					<tr>
						<td width="120" align="center">비밀번호</td>
						<td width="300"><input type="password" name="userPw" style="width: 95%;"></td>
					</tr>
				</table>
			</td>
		</tr>
		
		<tr>
			<!-- [수정] 오류 메시지를 표시할 공간 -->
			<td colspan="2" align="center">
				<span id="loginMessage" class="error-message"></span>
			</td>
		</tr>
		
		<tr>
			<td colspan="2" align="right" style="padding-top: 10px;">
				<!-- [수정] type을 'submit'에서 'button'으로 변경, id 부여 -->
				<button type="button" id="loginBtn">로그인</button>
				<button type="button" onclick="location.href='/board/boardList.do'" style="margin-left: 5px;">목록으로</button>
				<a href="/user/signup.do" style="margin-left: 10px;">회원가입</a>
			</td>
		</tr>
	</table>
</form>

</body>
</html>