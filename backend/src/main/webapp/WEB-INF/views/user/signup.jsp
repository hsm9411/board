<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<%@include file="/WEB-INF/views/common/common.jsp"%>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>회원가입</title>
<style>
    .message { font-size: 12px; margin-left: 10px; display: block; height: 14px; }
    .success { color: green; }
    .error { color: red; }
    .required { color: red; font-weight: bold; margin-left: 5px;}
    .guide { font-size: 12px; color: #888; margin-left: 10px; }
</style>
</head>
<script type="text/javascript">
    $j(document).ready(function(){
        
        var idCheckFlag = false;
        
        //================================================================
        // 헬퍼(Helper) 함수 영역
        //================================================================

        function clearErrorMessages() {
            $j(".message").text("").removeClass("success error");
        }

        function getByteLength(str) {
            if (str == null || str.length == 0) return 0;
            var byteLength = 0;
            for (var i = 0; i < str.length; i++) {
                var charCode = str.charCodeAt(i);
                if (charCode < 0x0080) byteLength += 1;
                else if (charCode < 0x0800) byteLength += 2;
                else byteLength += 3;
            }
            return byteLength;
        }
        
        //================================================================
        // 이벤트 핸들러(Event Handler) 영역
        //================================================================

        // '중복 확인' 버튼 클릭 이벤트
        $j("#idCheckBtn").on("click", function(){
            var userId = $j("input[name='userId']").val();
            var idMessage = $j("#idMessage");
            var idRegex = /^[a-zA-Z][a-zA-Z0-9]{3,14}$/;

            idMessage.text("");

            if(userId == ""){
                idMessage.text("아이디를 입력해주세요.").removeClass("success").addClass("error"); return;
            }
            if(!idRegex.test(userId)){
                idMessage.text("아이디는 영문자로 시작하는 4~15자 영문, 숫자만 가능합니다.").removeClass("success").addClass("error"); return;
            }
            
            $j.ajax({
                url: "/user/idCheck.do", type: "GET", data: { "userId": userId }, dataType: "json",
                success: function(result){
                    if(result == 1){
                        idMessage.text("이미 사용 중인 아이디입니다.").removeClass("success").addClass("error");
                        idCheckFlag = false;
                    } else {
                        idMessage.text("사용 가능한 아이디입니다.").removeClass("error").addClass("success");
                        idCheckFlag = true;
                    }
                },
                error: function(){ alert("서버와의 통신에 실패했습니다."); }
            });
        });

        // 아이디 입력창 keyup 이벤트
        $j("input[name='userId']").on("keyup", function(){
            idCheckFlag = false;
            $j("#idMessage").text("");
        });
        
        // 우편번호 입력창 keyup 이벤트
        $j("input[name='userAddr1']").on("keyup", function() {
            this.value = this.value.replace(/[^0-9]/g, '').replace(/^(\d{3})(\d{3})$/, `$1-$2`);
        });

        // 이름 입력창에 대한 실시간 바이트 길이 제한
        $j("input[name='userName']").on("keyup", function(e) {
            var input = $j(this);
            var text = input.val();
            var maxBytes = 15;
            var currentBytes = getByteLength(text);

            if (currentBytes > maxBytes) {
                var totalBytes = 0;
                var cutIndex = 0;
                for (var i = 0; i < text.length; i++) {
                    totalBytes += getByteLength(text.charAt(i));
                    if (totalBytes > maxBytes) {
                        cutIndex = i;
                        break;
                    }
                }
                var newText = text.substring(0, cutIndex);
                input.val(newText);
                
                // [수정] 오류 메시지를 표시하고, 1.5초 후 자동으로 지우도록 setTimeout 적용.
                var nameMessage = $j("#nameMessage");
                nameMessage.text("최대 입력 길이를 초과했습니다.").addClass("error");
                
                setTimeout(function() {
                    nameMessage.text("").removeClass("error");
                }, 1500);
            }
        });

        // 비밀번호 입력창 blur 이벤트 (실시간 검사)
        $j("input[name='userPw']").on("blur", function() {
            var userPw = $j(this).val();
            var pwMessage = $j("#pwMessage");
            var pwRegex = /^(?=.*[a-zA-Z])(?=.*[0-9]).{8,16}$/;

            if (userPw.trim() !== "") {
                if (!pwRegex.test(userPw)) {
                    pwMessage.text("비밀번호는 영문, 숫자 조합 8~16자 입니다.").removeClass("success").addClass("error");
                } else {
                    pwMessage.text("사용 가능한 비밀번호입니다.").removeClass("error").addClass("success");
                }
            } else {
                pwMessage.text("");
            }
        });

        // 비밀번호 확인 입력창 blur 이벤트 (실시간 검사)
        $j("input[name='userPwCheck']").on("blur", function() {
            var userPw = $j("input[name='userPw']").val();
            var userPwCheck = $j(this).val();
            var pwCheckMessage = $j("#pwCheckMessage");
            
            if (userPwCheck.trim() !== "") {
                if (userPw !== userPwCheck) {
                    pwCheckMessage.text("비밀번호가 일치하지 않습니다.").removeClass("success").addClass("error");
                } else {
                    pwCheckMessage.text("비밀번호가 일치합니다.").removeClass("error").addClass("success");
                }
            } else {
                pwCheckMessage.text("");
            }
        });

        // '가입하기' 버튼(#signupBtn) 클릭 시 최종 유효성 검사 및 서버 전송
        $j("#signupBtn").on("click", function(){
            
            clearErrorMessages();
            var isValid = true;

            // --- 최종 클라이언트 유효성 검사 ---
            if ($j("input[name='userId']").val().trim() === "") {
                $j("#idMessage").text("아이디를 입력해주세요.").addClass("error");
                isValid = false;
            } else if (!idCheckFlag) {
                $j("#idMessage").text("아이디 중복 확인을 해주세요.").addClass("error");
                isValid = false;
            }
            
            var userPw = $j("input[name='userPw']").val();
            var pwRegex = /^(?=.*[a-zA-Z])(?=.*[0-9]).{8,16}$/;
            if (userPw.trim() === "") {
                $j("#pwMessage").text("비밀번호를 입력해주세요.").addClass("error");
                isValid = false;
            } else if (!pwRegex.test(userPw)) {
                $j("#pwMessage").text("비밀번호는 영문, 숫자 조합 8~16자 입니다.").addClass("error");
                isValid = false;
            }

            if ($j("input[name='userPwCheck']").val().trim() === "") {
                $j("#pwCheckMessage").text("비밀번호 확인을 입력해주세요.").addClass("error");
                isValid = false;
            } else if (userPw !== $j("input[name='userPwCheck']").val()) {
                $j("#pwCheckMessage").text("비밀번호가 일치하지 않습니다.").addClass("error");
                isValid = false;
            }
            
            var userName = $j("input[name='userName']").val();
            if (userName.trim() === "") {
                $j("#nameMessage").text("이름을 입력해주세요.").addClass("error");
                isValid = false;
            } else if (getByteLength(userName) > 15) {
                $j("#nameMessage").text("이름이 너무 깁니다. (한글 5자, 영문 15자 이내)").addClass("error");
                isValid = false;
            }
            
            if ($j("input[name='userPhone2']").val().trim() === "" || $j("input[name='userPhone3']").val().trim() === "") {
                $j("#phoneMessage").text("전화번호를 모두 입력해주세요.").addClass("error");
                isValid = false;
            }

            if (!isValid) return;

            var param = $j("#signupForm").serialize();

            $j.ajax({
                url: "/user/signupAction.do", type: "POST", dataType: "json", data: param,
                success: function(response) {
                    if(response.status === "success") {
                        alert(response.message);
                        location.href = response.redirectUrl;
                    } else {
                        alert("입력 내용을 다시 확인해주세요.");
                        if (response.errors) {
                            if (response.errors.userId)   { $j("#idMessage").text(response.errors.userId).addClass("error"); }
                            if (response.errors.userPw)    { $j("#pwMessage").text(response.errors.userPw).addClass("error"); }
                            if (response.errors.userName)  { $j("#nameMessage").text(response.errors.userName).addClass("error"); }
                            if (response.errors.userPhone) { $j("#phoneMessage").text(response.errors.userPhone).addClass("error"); }
                        }
                    }
                },
                error: function () { alert("처리 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요."); }
            });
        });
    });
</script>
<body>

<form id="signupForm">
    <table align="center">
        <tr><td colspan="2" align="center"><h2>회원가입</h2></td></tr>
        <tr>
            <td colspan="2">
                <table border="1" style="width: 100%;">
                    <colgroup>
                        <col width="150px" />
                        <col width="*" />
                    </colgroup>
                    <tr>
                        <td align="center">아이디<span class="required">*</span></td>
                        <td>
                            <input type="text" name="userId" maxlength="15" style="width: 200px;">
                            <button type="button" id="idCheckBtn">중복 확인</button>
                            <span id="idMessage" class="message"></span>
                        </td>
                    </tr>
                    <tr>
                        <td align="center">비밀번호<span class="required">*</span></td>
                        <td>
                            <input type="password" name="userPw" maxlength="16">
                            <span class="guide">8~16자 영문, 숫자 조합</span>
                            <span id="pwMessage" class="message"></span>
                        </td>
                    </tr>
                    <tr>
                        <td align="center">비밀번호 확인<span class="required">*</span></td>
                        <td>
                            <input type="password" name="userPwCheck" maxlength="16">
                            <span id="pwCheckMessage" class="message"></span>
                        </td>
                    </tr>
                    <tr>
                        <td align="center">이름<span class="required">*</span></td>
                        <td>
                            <input type="text" name="userName" maxlength="15">
                            <span class="guide">한글 5자, 영문 15자 이내</span>
                            <span id="nameMessage" class="message"></span>
                        </td>
                    </tr>
                    <tr>
                        <td align="center">전화번호<span class="required">*</span></td>
                        <td>
                            <select name="userPhone1" style="width: 80px;">
                                <c:forEach var="code" items="${phoneCodeList}"><option value="${code.codeId}">${code.codeName}</option></c:forEach>
                            </select>
                            - <input type="text" name="userPhone2" maxlength="4" style="width: 80px;">
                            - <input type="text" name="userPhone3" maxlength="4" style="width: 80px;">
                            <span id="phoneMessage" class="message"></span>
                        </td>
                    </tr>
                    <tr>
                        <td align="center">우편번호</td>
                        <td>
                            <input type="text" name="userAddr1" maxlength="7" style="width: 100px;">
                        </td>
                    </tr>
                    <tr>
                        <td align="center">주소</td>
                        <td><input type="text" name="userAddr2" maxlength="150" style="width: 95%;"></td>
                    </tr>
                    <tr>
                        <td align="center">회사명</td>
                        <td><input type="text" name="userCompany" maxlength="60" style="width: 95%;"></td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td colspan="2" align="right" style="padding-top: 10px;">
                <button type="button" id="signupBtn">가입하기</button>
                <button type="button" onclick="location.href='/board/boardList.do'">목록으로</button>
            </td>
        </tr>
    </table>
</form>

</body>
</html>