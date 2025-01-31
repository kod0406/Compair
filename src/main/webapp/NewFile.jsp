<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.sql.*" %>
<%@page import="DAO.UserDAO" %>
<% 
	request.setCharacterEncoding("utf-8");
	
	String uid = request.getParameter("id");
	String upass = request.getParameter("password");
	
	System.out.println(uid);
	System.out.println(upass);
	
    UserDAO user = new UserDAO();
	if (user.insert(uid, upass)) {
	    //out.print("회원 가입이 완료되었습니다.");
	    out.print("OK");
	}
%>