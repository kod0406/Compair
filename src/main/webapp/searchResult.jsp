<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="DAO.UserDAO" %>
<%
    request.setCharacterEncoding("UTF-8");
    String email = request.getParameter("user_mail");
    
    UserDAO dao = new UserDAO();
    String[] userInfo = dao.getUserInfoByEmail(email);
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>검색 결과</title>
</head>
<body>
    <h2>검색 결과</h2>
    <% if(userInfo[0] != null) { %>
        <p>ID: <%= userInfo[0] %></p>
        <p>Password: <%= userInfo[1] %></p>
    <% } else { %>
        <p>해당 이메일로 등록된 사용자가 없습니다.</p>
    <% } %>
    <a href="searchForm.jsp">다시 검색</a>
    <a href="login.jsp">매인화면으로 이동</a>
</body>
</html>