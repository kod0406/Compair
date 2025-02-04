<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="DAO.UserDAO" %>
<%
    // 세션 체크 로직
    String userID = null;
    if (session.getAttribute("uid") != null) {
        userID = (String) session.getAttribute("uid");
    }
    if (userID != null) {
        out.println("<script>");
        out.println("alert('이미 로그인이 되어있습니다.')");
        out.println("location.href='main.html';");
        out.println("</script>");
        // 기존 로직이 더 이상 실행되지 않도록 return
        return;
    }

    request.setCharacterEncoding("utf-8");

    String uid = request.getParameter("id");
    String upass = request.getParameter("password");
    String email = request.getParameter("mail");
    String name = request.getParameter("name");

    UserDAO user = new UserDAO();

    // 중복 체크
    if (user.isIdExist(uid)) {
        out.print("ID_EXIST");
    } else if (user.isEmailExist(email)) {
        out.print("EMAIL_EXIST");
    } else {
        // 회원가입 진행
        if (user.insert(uid, upass, email, name)) {
            out.print("OK");
        } else {
            out.print("ER");
        }
    }
%>
