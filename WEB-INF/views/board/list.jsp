<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.javaex.dao.ktu.BoardDao"%>
<%@ page import="com.javaex.dao.ktu.BoardDaoImpl"%>
<%@ page import="com.javaex.vo.ktu.BoardVo"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link href="/mysite/assets/css/board.css" rel="stylesheet"
	type="text/css">
<title>Mysite</title>
<script type="text/javascript">
	// 추가한 내용
	function list() {
		document.listFrm.action = "/mysite/board";
		document.listFrm.submit();
	}
	function pageing(page) {
		document.readFrm.nowPage.value = page;
		if ('${kewWord}' != "") {
			document.readFrm.keyWord.value = '${kewWord}';
			document.readFrm.keyField.value = '${keyField}';
		}
		//document.readFrm.action = "/mysite/board";      
		document.readFrm.submit();
	}

	function block(value) {
		var param = '${pagePerBlock}';
		document.readFrm.nowPage.value = parseInt(param) * (value - 1) + 1; // 5 x (2 - 1) + 1 = 6
		if ('${kewWord}' != "") {
			document.readFrm.keyWord.value = '${kewWord}';
			document.readFrm.keyField.value = '${keyField}';
		}
		document.readFrm.submit();
	}

	function read(num) {
		document.readFrm.num.value = num;
		document.readFrm.action = "/mysite/board";
		document.readFrm.submit();
	}

	function check() { // 검색어 입력 여부 확인
		if (document.searchFrm.keyWord.value == "") {
			alert("검색어를 입력하세요.");
			document.searchFrm.keyWord.focus();
			return;
		}
		alert(document.searchFrm.keyWord.value);
		document.searchFrm.action = "/mysite/board?a=list";
		document.searchFrm.submit();

	}
</script>
</head>
<body>
	<div id="container">

		<c:import url="/WEB-INF/views/includes/header.jsp"></c:import>
		<c:import url="/WEB-INF/views/includes/navigation.jsp"></c:import>

		<div id="content">
			<div id="board">
				<form id="searchFrm" name="searchFrm" method="post">
					<table width="600" cellpadding="4" cellspacing="0">
						<tr>
							<td align="center" valign="bottom"><select name="keyField"
								size="1">
									<option value="name">글쓴이</option>
									<option value="reg_date">작성일</option>
									<option value="title">제 목</option>
									<option value="content">내 용</option>
							</select> <input size="16" name="keyWord" value=""> <input
								type="button" value="찾기" onClick="javascript:check()"> <input
								type="hidden" name="nowPage" value="1"></td>
						</tr>
					</table>
				</form>
				<table class="tbl-ex">
					<tr>
						<th>번호</th>
						<th>제목</th>
						<th>글쓴이</th>
						<th>조회수</th>
						<th>작성일</th>
						<th>&nbsp;</th>
					</tr>
					<c:forEach var="vo" items="${vlist}" begin="0" step="1"
						end="${numPerPage}">
						<tr>
							<td>${vo.no }</td>
							<td><a href="/mysite/board?a=read&no=${vo.no}">${vo.title }</a></td>
							<td>${vo.userName }</td>
							<td>${vo.hit }</td>
							<td>${vo.regDate }</td>
							<td><c:if test="${authUser.no == vo.userNo }">
									<a href="/mysite/board?a=delete&no=${vo.no }" class="del">삭제</a>
								</c:if></td>
						</tr>
					</c:forEach>
				</table>

				<div class="pager">
					<ul>
						<!-- 페이징 및 블럭 처리 Start-->
						<c:if test="${not empty totalPage}">
							<c:if test="${nowBlock > 1}">
								<a href="javascript:block(${nowBlock-1});">prev...</a>
							</c:if>&nbsp;    			  		    			  		
    					<c:forEach var="cur" begin="${pageStart}" end="${pageEnd}">
								<a href="javascript:pageing(${cur});"> [${cur}] </a>
							</c:forEach>&nbsp;
   						<c:if test="${totalBlock > nowBlock}">
								<a href="javascript:block(${nowBlock+1});">.....next</a>
							</c:if>&nbsp;
    			  </c:if>
						<!-- 페이징 및 블럭 처리 End-->
					</ul>
				</div>
				<!--  listFrm, readFrm 만들기 -->
				<form name="listFrm" method="post" action="">
					<input type="hidden" name="reload" value="true"> <input
						type="hidden" name="nowPage" value="1">

				</form>


				<form name="readFrm" method="get">
					<input type="hidden" name="a" value="list"> <input
						type="hidden" name="nowPage" value="${nowPage}"> <input
						type="hidden" name="keyField" value="${keyField}"> <input
						type="hidden" name="keyWord" value="${keyWord}">

				</form>
				<c:if test="${authUser != null }">
					<div class="bottom">
						<a href="/mysite/board?a=writeform" id="new-book">글쓰기</a>
					</div>
				</c:if>
			</div>
		</div>

		<c:import url="/WEB-INF/views/includes/footer.jsp"></c:import>

	</div>
	<!-- /container -->
</body>
</html>

