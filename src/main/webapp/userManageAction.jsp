<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="DAO.UserDAO" %>
<%
	UserDAO dao = new UserDAO();
	String action = request.getParameter("action");
	String userId = request.getParameter("userId");
	
	try {
	    if("approve".equals(action)) {
	        dao.approveUser(userId);
	    } else if("reject".equals(action)) {
	        boolean deleteResult = dao.rejectUser(userId); // 삭제 결과 확인 가능
	    }
	} catch(Exception e) {
	    e.printStackTrace();
	}
%>