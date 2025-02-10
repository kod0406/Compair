<%@ page language="java" contentType="text/plain; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="DAO.UserDAO" %>

<%
    request.setCharacterEncoding("UTF-8");
    String email = request.getParameter("user_mail");

    UserDAO dao = new UserDAO();
    String[] userInfo = dao.getUserInfoByEmail(email);

    if (userInfo != null && userInfo.length > 1) {
        out.print(userInfo[0] + "," + userInfo[1]); // 콤마(,)로 구분
    } else {
        out.print("fail");
    }
%>
