<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<%@include file="/WEB-INF/views/common/common.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>게시판 목록</title>
</head>
<script type="text/javascript">
	$j(document).ready(function(){
		
		// '전체' 체크박스 관련 로직 (기존과 동일)
		$j("#checkAll").click(function(){
			const isChecked = $j(this).is(":checked");
			$j("input[name='boardTypes']").prop('checked', isChecked);
		});
		
		$j("input[name='boardTypes']").click(function(){
			const totalCount = $j("input[name='boardTypes']").length;
			const checkedCount = $j("input[name='boardTypes']:checked").length;
			
			if (totalCount === checkedCount) {
				$j("#checkAll").prop('checked', true);
			} else {
				$j("#checkAll").prop('checked', false);
			}
		});

		// '검색' 버튼 클릭 이벤트 (기존과 동일)
		$j("#searchBtn").click(function(){
			let url = "/board/boardList.do?pageNo=1";
			$j("input[name='boardTypes']:checked").each(function(){
				url += "&boardTypes=" + $j(this).val();
			});
			location.href = url;
		});

		// 페이지 링크 클릭 이벤트 (data-page 속성 값으로 페이지 이동)
		$j(".page-link").click(function(e){
			e.preventDefault(); 
			const pageNo = $j(this).data("page");
			if (pageNo) {
				goPage(pageNo);
			}
		});
		
		$j(".page-link").css("text-decoration", "none");
	});

    // 페이징 이동 함수 (기존과 동일)
    function goPage(pageNo) {
        let url = "/board/boardList.do?pageNo=" + pageNo;
        $j("input[name='boardTypes']:checked").each(function(){
            url += "&boardTypes=" + $j(this).val();
        });
        location.href = url;
    }
</script>
<body>

<%-- 상단 로그인/회원가입 부분 (기존과 동일) --%>
<div align="right">
    <c:if test="${empty sessionScope.loginUser}">
        <button type="button" onclick="location.href='/user/login.do'">로그인</button> | 
        <button type="button" onclick="location.href='/user/signup.do'">회원가입</button>
    </c:if>
    <c:if test="${not empty sessionScope.loginUser}">
        ${sessionScope.loginUser.userName}님 | <button type="button" onclick="location.href='/user/logout.do'">로그아웃</button>
    </c:if>
</div>
<br>

<table align="center">
	<%-- 테이블 상단 및 게시글 목록 부분 (기존과 동일) --%>
	<tr>
		<td align="right">
			total : ${pageVo.totalCnt}
		</td>
	</tr>
	<tr>
		<td>
			<table id="boardTable" border="1">
				<tr>
					<td width="80" align="center">Type</td>
					<td width="40" align="center">No</td>
					<td width="300" align="center">Title</td>
				</tr>
				
				<c:if test="${empty boardList}">
					<tr>
						<td colspan="3" align="center">게시글이 없습니다.</td>
					</tr>
				</c:if>

				<c:forEach items="${boardList}" var="list">
					<tr>
						<td align="center">${list.boardTypeName}</td>
						<td align="center">${list.boardNum}</td>
						<td>
							<c:url var="viewUrl" value="/board/${list.boardType}/${list.boardNum}/boardView.do">
								<c:param name="pageNo" value="${pageVo.pageNo}" />
								<c:forEach var="type" items="${pageVo.boardTypes}">
									<c:param name="boardTypes" value="${type}" />
								</c:forEach>
							</c:url>
							<a href="${viewUrl}">${list.boardTitle}</a>
						</td>
					</tr>	
				</c:forEach>
			</table>
		</td>
	</tr>
	<tr>
		<td align="right" style="padding-top: 5px;">
			<a href="/board/boardWrite.do" style="margin-right: 5px;">글쓰기</a>
		</td>
	</tr>
	<tr>
		<td align="center" style="padding-top: 20px;">
			<input type="checkbox" id="checkAll"/> 전체
			
			<c:forEach items="${boardTypeList}" var="boardType">
				<input type="checkbox" name="boardTypes" value="${boardType.codeId}" 
					<c:forEach var="selectedType" items="${pageVo.boardTypes}">
						<c:if test="${selectedType eq boardType.codeId}">checked</c:if>
					</c:forEach>
				/> ${boardType.codeName}
			</c:forEach>
			
			<button type="button" id="searchBtn" style="margin-left: 15px;">검색</button>
		</td>
	</tr>

	<%-- ===================== [수정된 페이징 표시 부분] ===================== --%>
	<tr>
		<td align="center" style="padding-top: 20px;">
			<div>
                <%-- '처음' 버튼 --%>
                <a href="#" class="page-link" data-page="1" style="margin: 0 2px;">[처음]</a>

				<%-- '이전' 버튼: pageVo의 prev가 true일 때만 활성화 --%>
                <c:if test="${pageVo.prev}">
                    <a href="#" class="page-link" data-page="${pageVo.startPage - 1}" style="margin: 0 2px;">[이전]</a>
                </c:if>
				<c:if test="${!pageVo.prev}">
					<span style="margin: 0 2px; color: gray;">[이전]</span>
				</c:if>

				<%-- 페이지 번호 목록: 서버에서 계산된 startPage와 endPage를 사용 --%>
				<c:forEach begin="${pageVo.startPage}" end="${pageVo.endPage}" var="page">
				    <c:choose>
				        <c:when test="${pageVo.pageNo == page}">
				            <span style="margin: 0 2px; color: black; font-weight: bold;">
				                [${page}]
				            </span>
				        </c:when>
				        <c:otherwise>
				            <a href="#" class="page-link" data-page="${page}" style="margin: 0 2px;">
				                [${page}]
				            </a>
				        </c:otherwise>
				    </c:choose>
				</c:forEach>

				<%-- '다음' 버튼: pageVo의 next가 true일 때만 활성화 --%>
                <c:if test="${pageVo.next}">
                    <a href="#" class="page-link" data-page="${pageVo.endPage + 1}" style="margin: 0 2px;">[다음]</a>
                </c:if>
				<c:if test="${!pageVo.next}">
					<span style="margin: 0 2px; color: gray;">[다음]</span>
				</c:if>

                <%-- '끝' 버튼 --%>
                <a href="#" class="page-link" data-page="${pageVo.totalPage}" style="margin: 0 2px;">[끝]</a>
			</div>
		</td>
	</tr>
	<%-- ================================================================= --%>
</table>	
</body>
</html>