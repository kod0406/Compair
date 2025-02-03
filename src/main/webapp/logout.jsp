<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    session.invalidate(); // 세션 종료
    response.sendRedirect("index.jsp"); // 로그아웃 후 로그인 페이지로 이동
%>