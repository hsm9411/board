<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@include file="/WEB-INF/views/common/common.jsp"%>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>게시글 수정</title>
</head>
<script type="text/javascript">
	$j(document).ready(function(){
		
		// '수정' 버튼 클릭 시 AJAX로 데이터 전송
		$j("#submit").on("click",function(){
			var $frm = $j('.boardEdit :input');
			var param = $frm.serialize();
			
			$j.ajax({
			    url : "/board/boardUpdateAction.do", // 수정 처리는 이 URL에서 담당
			    dataType: "json",
			    type: "POST",
			    data : param,
			    success: function(data, textStatus, jqXHR)
			    {
					if(data.success == "Y"){
						alert("게시글이 성공적으로 수정되었습니다.");
						// 수정 완료 후, 수정된 게시글의 상세 보기 페이지로 이동합니다.
						// Controller에서 받은 board 객체의 boardType과 boardNum을 사용합니다.
						location.href = "/board/${board.boardType}/${board.boardNum}/boardView.do";
					} else {
						alert("수정 중 오류가 발생했습니다.");
					}
			    },
			    error: function (jqXHR, textStatus, errorThrown)
			    {
			    	alert("서버와의 통신에 실패했습니다.");
			    }
			});
		});
	});
</script>
<body>
<form class="boardEdit">
	<%-- [핵심] Controller에서 Model에 담아준 "board" 객체의 값을 사용합니다. --%>

	<%-- 수정할 대상이 누구인지 서버에 알려주기 위한 hidden input --%>
	<input type="hidden" name="boardNum" value="${board.boardNum}" />
    <input type="hidden" name="boardType" value="${board.boardType}" />

	<table align="center">
		<tr>
			<td>
				<table border ="1"> 
					<tr>
						<td width="120" align="center">Title</td>
						<td width="400">
							<%-- 기존 게시글 제목을 value에 출력합니다. --%>
							<input name="boardTitle" type="text" size="50" value="${board.boardTitle}"> 
						</td>
					</tr>
					<tr>
						<td height="300" align="center">Comment</td>
						<td valign="top">
							<%-- 기존 게시글 내용을 textarea 안에 출력합니다. --%>
							<textarea name="boardComment" rows="20" cols="55">${board.boardComment}</textarea>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td align="right" style="padding-top: 10px;">
				<input id="submit" type="button" value="수정">
				<button type="button" onclick="location.href='/board/${board.boardType}/${board.boardNum}/boardView.do'">
				    취소
				</button>
				
				<button type="button" onclick="location.href='/board/boardList.do'">
				    목록
				</button>
			</td>
		</tr>
	</table>
</form>	
</body>
</html>