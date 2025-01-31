<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.sql.*" %>
<%@page import="DAO.UserDAO" %>
<% 
    request.setCharacterEncoding("utf-8");
    
    String uid = request.getParameter("id");
    String upass = request.getParameter("password");
    String email = request.getParameter("mail");
    String name = request.getParameter("name");
    
    UserDAO user = new UserDAO();
    
    // 중복 체크
    if (user.isIdExist(uid)) {
        out.print("ID_EXIST"); // 중복된 ID
    } else if (user.isEmailExist(email)) {
        out.print("EMAIL_EXIST"); // 중복된 이메일
    } else {
        // 중복 없으면 회원가입 진행
        if (user.insert(uid, upass, email, name)) {
            out.print("OK"); // 회원가입 성공
        } else {
            out.print("ER"); // 회원가입 실패
        }
    }
%>