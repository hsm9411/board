<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@include file="/WEB-INF/views/common/common.jsp"%>    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>게시글 상세</title>
<script>
	function confirmDelete() {
		if (confirm("정말로 삭제하시겠습니까?")) {
			document.getElementById("deleteForm").submit();
		}
	}
</script>
</head>
<body>
<table align="center">
	<tr>
		<td>
			<table border ="1">
				<tr>
					<td width="120" align="center">Title</td>
					<!-- 이제 'board'는 BoardResponseDto 객체입니다. -->
					<td width="400">${board.boardTitle}</td>
				</tr>
				<tr>
					<td height="300" align="center">Comment</td>
					<td>${board.boardComment}</td>
				</tr>
				<tr>
					<td align="center">Writer</td>
					<!-- DTO에 의해 '익명' 처리된 작성자 이름이 출력됩니다. -->
					<td>${board.creator}</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td align="right" style="padding-top: 10px;">
			<form id="deleteForm" action="/board/boardDelete.do" method="post" style="display: inline;">
				<input type="hidden" name="boardNum" value="${board.boardNum}">
				<input type="hidden" name="boardType" value="${board.boardType}">
			</form>
		
			<button type="button" onclick="location.href='/board/boardList.do'">목록</button>
			
			<%-- 
			  [핵심 수정] 
			  이제 복잡한 JSTL 조건문 대신, 컨트롤러가 계산해준 'board.isOwner' 값을 직접 사용합니다.
			  이 코드는 "Model에 담긴 board 객체의 isOwner() 메소드를 호출하라"는 의미입니다.
			--%>
			<c:if test="${board.isOwner}">
				<button type="button" onclick="location.href='/board/${board.boardType}/${board.boardNum}/boardEdit.do'" style="margin-left: 5px;">수정</button>
				<button type="button" onclick="confirmDelete()" style="margin-left: 5px;">삭제</button>
			</c:if>
		</td>
	</tr>
</table>	
</body>
</html>