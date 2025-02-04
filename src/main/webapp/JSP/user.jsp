<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="DAO.UserDAO" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>

<%
    session = request.getSession(false); // 기존 세션 확인
    String userId = (session != null) ? (String) session.getAttribute("uid") : null;

    if (userId != null) {
        UserDAO userDAO = new UserDAO();
        String[] userInfo = userDAO.getUserInfoById(userId); // 사용자 정보 가져오기

        if (userInfo != null && userInfo.length == 4) {
%>
            <!-- 사용자 정보 HTML 출력 -->
            <table class="table">
                <tr>
                    <td><b>User ID</b></td>
                    <td><%= userInfo[0] %></td>
                </tr>
                <tr>
                    <td><b>Name</b></td>
                    <td><%= userInfo[1] %></td>
                </tr>
                <tr>
                    <td><b>Email</b></td>
                    <td><%= userInfo[2] %></td>
                </tr>
                <tr>
                    <td><b>Password</b></td>
                    <td><%= userInfo[3] %></td>
                </tr>
            </table>
<%
        } else {
            out.print("<p>사용자 정보를 불러올 수 없습니다.</p>");
        }
    } else {
        out.print("<p>로그인하지 않았습니다. <a href='../html/login.html'>로그인</a> 하세요.</p>");
    }
%>
