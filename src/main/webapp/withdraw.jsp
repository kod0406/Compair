<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="DAO.UserDAO" %>
<%@ page import="java.io.PrintWriter" %>

<%
    request.setCharacterEncoding("UTF-8");
    String uid = request.getParameter("uid");

    UserDAO userDAO = new UserDAO();
    boolean isDeleted = userDAO.deleteUser(uid);  // 회원탈퇴 메서드 호출

    response.setContentType("text/html;charset=UTF-8");

    if (isDeleted) {
        session.invalidate();  // 탈퇴 후 세션 종료
        out.println("<script>alert('회원 탈퇴가 완료되었습니다.'); window.location.href='login.jsp';</script>");
    } else {
        out.println("<script>alert('회원 탈퇴에 실패했습니다.'); history.back();</script>");
    }
%>
