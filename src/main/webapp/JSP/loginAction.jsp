<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="DAO.UserDAO" %>
<%@ page import="user.User" %>
<%@ page import="java.io.PrintWriter" %>
<% request.setCharacterEncoding("UTF-8"); %>
<jsp:useBean id="user" class="user.User" scope="page" />
<jsp:setProperty name="user" property="uid" />
<jsp:setProperty name="user" property="upass" />
<%
	UserDAO userDAO = new UserDAO();
	int result = userDAO.login(user.getUid(), user.getUpass());
	switch(result) {
		case 1: 
			session.setAttribute("uid", user.getUid());
			response.sendRedirect("../html/main.html");
			break;
		case 0:
			%><script>alert('비밀번호가 틀렸습니다.'); history.back();</script><% 
			break;
		case -1:
			%><script>alert('아이디가 없습니다.'); history.back();</script><% 
			break;
		default:
			%><script>alert('서버 오류가 발생했습니다.'); history.back();</script><%
	}
%>