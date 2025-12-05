<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@include file="/WEB-INF/views/common/common.jsp"%>    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> <%-- JSTL 사용을 위해 이 줄은 반드시 필요합니다. --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>boardWrite</title>
</head>
<script type="text/javascript">

	$j(document).ready(function(){
		
		// [기능1] 로그인하지 않은 사용자는 글을 쓸 수 없도록 처리
		<c:if test="${empty sessionScope.loginUser}">
			alert("로그인 후 이용 가능합니다.");
			location.href = "/user/login.do"; // 실제 사용하는 로그인 페이지 URL로 변경하세요.
		</c:if>
		
		$j("#submit").on("click",function(){
			var $frm = $j('.boardWrite :input');
			var param = $frm.serialize();
			
			$j.ajax({
			    url : "/board/boardWriteAction.do",
			    dataType: "json",
			    type: "POST",
			    data : param,
			    success: function(data, textStatus, jqXHR)
			    {
					alert("게시글이 성공적으로 등록되었습니다.");
					location.href = "/board/boardList.do?pageNo=1";
			    },
			    error: function (jqXHR, textStatus, errorThrown)
			    {
			    	alert("게시글 등록에 실패했습니다.");
			    }
			});
		});
	});
	

</script>
<body>
<form class="boardWrite">
	<table align="center">
		<tr>
			<td align="right">
			<input id="submit" type="button" value="작성">
			</td>
		</tr>
		<tr>
			<td>
				<table border ="1"> 
					<%-- [기능2] Type을 선택하는 기능 추가 --%>
					<tr>
						<td width="120" align="center">
						Type
						</td>
						<td width="400">
							<select name="boardType">
								<%-- 컨트롤러가 전달한 boardTypeList를 사용하여 반복문을 돕니다. --%>
								<c:forEach items="${boardTypeList}" var="code">
									<%-- value에는 코드ID('a01'), 화면에는 코드이름('일반')을 보여줍니다. --%>
									<option value="${code.codeId}">${code.codeName}</option>
								</c:forEach>
							</select>
						</td>
					</tr>
					<tr>
						<td width="120" align="center">
						Title
						</td>
						<td width="400">
						<input name="boardTitle" type="text" size="50" value="${board.boardTitle}"> 
						</td>
					</tr>
					<tr>
						<td height="300" align="center">
						Comment
						</td>
						<td valign="top">
						<textarea name="boardComment"  rows="20" cols="55">${board.boardComment}</textarea>
						</td>
					</tr>
					<%-- [기능3] 로그인한 사용자의 이름을 Writer로 자동 설정 --%>
					<tr>
						<td align="center">
						Writer
						</td>
						<td>
							<%-- 화면에는 세션에 저장된 사용자 이름을 보여줌 --%>
							${sessionScope.loginUser.userName}
							
							<%-- 실제 서버로 전송될 데이터 (BoardVo의 creator 필드와 매핑) --%>
							<input type="hidden" name="creator" value="${sessionScope.loginUser.userName}">
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td align="right">
				<a href="/board/boardList.do">List</a>
			</td>
		</tr>
	</table>
</form>	
</body>
</html>