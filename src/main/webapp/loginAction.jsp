<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="DAO.UserDAO" %>
<%@ page import="user.User" %>
<%@ page import="java.io.PrintWriter" %>
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
	String userID = null;
	if (session.getAttribute("uid") != null) {
		userID = (String) session.getAttribute("uid");
	}
	if(userID != null) {
		PrintWriter script = response.getWriter();
		script.println("<script>");
		script.println("alert('이미 로그인이 되어있습니다.')");
		script.println("location.href = 'main.jsp'");
		script.println("</script>");
	}	
	UserDAO userDAO = new UserDAO();
	int result = userDAO.login(user.getUid(), user.getUpass());
	switch(result) {
		case 1: 
			session.setAttribute("uid", user.getUid());
			response.sendRedirect("main.jsp");
			break;
		case 0:
			%><script>alert('비밀번호가 틀렸습니다.'); history.back();</script><% 
			break;
		case 2:
			%><script>alert('관리자 승인 대기 중입니다.'); history.back();</script><% 
			break;
		case -1:
			%><script>alert('아이디가 없습니다.'); history.back();</script><% 
			break;
		default:
			%><script>alert('데이터베이스 오류가 발생했습니다.'); history.back();</script><%
	}
	%>
</body>
</html>