<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="DAO.UserDAO" %>
<%@ page import="java.io.PrintWriter" %>
<%
    String uid = request.getParameter("uid");
    String username = request.getParameter("username");
    String usermail = request.getParameter("usermail");
    String userpass = request.getParameter("userpass");

    UserDAO userDAO = new UserDAO();
    boolean isUpdated = userDAO.updateUserInfo(uid, username, usermail, userpass);  // 사용자 정보 업데이트
    
    if (isUpdated) {
        out.println("<script>alert('사용자 정보가 수정되었습니다.'); window.location.href='user.jsp';</script>");
    } else {
        out.println("<script>alert('수정에 실패했습니다.'); history.back();</script>");
    }
%>
