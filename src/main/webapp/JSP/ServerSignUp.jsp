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
	String uid = (String) session.getAttribute("uid");
	String scodeStr = request.getParameter("scode");
	int scode = Integer.parseInt(scodeStr);
	UserDAO sudao = new UserDAO();
	int errCode = sudao.serverInsert(uid, scode);
	
	switch(errCode) {
		case 1: 
			response.sendRedirect("../html/main.html");
			break;
		case 0:
			%><script>alert('해당 서버가 존재하지 않습니다.'); history.back();</script><% 
			break;
		default:
			%><script>alert('데이터베이스 오류가 발생했습니다.'); history.back();</script><%
	}
	%>
</body>
</html>