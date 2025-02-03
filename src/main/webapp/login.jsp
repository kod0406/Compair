<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.io.PrintWriter"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login</title>
</head>
<body>
	<%
	String userID = null;
	if (session.getAttribute("uid") != null) {
		userID = (String) session.getAttribute("uid");
	}
	if (userID != null) {
		PrintWriter script = response.getWriter();
		script.println("<script>");
		script.println("alert('이미 로그인이 되어있습니다.')");
		script.println("location.href = 'main.jsp'");
		script.println("</script>");
	}
	%>
	<form action="loginAction.jsp" method="post">
		<div class="from-group">
			<label for="id">아이디</label> <input type="text" id="id" name="uid"
				placeholder="아이디를 입력하세요" required />
		</div>
		<div class="from-group">
			<label for="id">비밀번호</label> <input type="password" id="password"
				name="upass" placeholder="비밀번호를 입력하세요" required />
		</div>
		<input type="submit" value="로그인">
	</form>
	<a href="searchForm.jsp">비밀번호 찾기</a>
	<a href="NewFile.html">회원가입</a>
</body>
</html>