<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "DAO.UserDAO" %>
<%@ page import = "user.User" %>  <!-- user 패키지의 User 클래스 임포트 추가 -->
<%@ page import ="java.io.PrintWriter" %>
<% request.setCharacterEncoding("UTF-8"); %>
<jsp:useBean id="user" class="user.User" scope="page" />
<jsp:setProperty name="user" property="uid" />
<jsp:setProperty name="user" property="upass" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login</title>
</head>
<body>
	<%
	UserDAO userDAO = new UserDAO();
	int result = userDAO.login(user.getUid(), user.getUpass()); 
	if (result == 1) {
		PrintWriter script = response.getWriter();
		script.println("<script>");
		script.println("location.href = 'main.html'");
		script.println("</script>");
	} else if (result == 0) {
		PrintWriter script = response.getWriter();
		script.println("<script>");
		script.println("alert('비밀번호가 틀렸습니다.')");
		script.println("history.back()");
		script.println("</script>");
	} else if (result == -1) {
		PrintWriter script = response.getWriter();
		script.println("<script>");
		script.println("alert('아이디가 없습니다.')");
		script.println("history.back()");
		script.println("</script>");
	} else if (result == -2) {
		PrintWriter script = response.getWriter();
		script.println("<script>");
		script.println("alert('데이터베이스 오류가 발생했습니다.')");
		script.println("history.back()");
		script.println("</script>");
	}
	%>
</body>
</html>