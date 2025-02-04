<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="DAO.UserDAO" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>

<html lang="ko">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>내 정보</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet" />
</head>
<body>
    <!-- NavBar -->
    <nav class="navbar navbar-expand-lg navbar-custom fixed-top">
      <div class="container-fluid">
        <a class="navbar-brand" href="main.html">Com-pair</a>
      </div>
    </nav>

    <div class="container mt-5">
        <h1>내 정보 수정</h1>
        <%
            session = request.getSession(false); // 기존 세션 확인
            if (session != null && session.getAttribute("uid") != null) {
                String userId = (String) session.getAttribute("uid");
                UserDAO userDAO = new UserDAO();
                String[] userInfo = userDAO.getUserInfoById(userId); // 사용자 정보 가져오기

                if (userInfo != null && userInfo.length == 4) {
        %>
        <!-- 사용자 정보 수정 폼 -->
        <form action="../JSP/userupdate.jsp" method="post">
            <div class="mb-3">
                <label for="userId" class="form-label">User ID</label>
                <input type="text" class="form-control" id="userId" name="userId" value="<%= userInfo[0] %>" readonly>
            </div>
            <div class="mb-3">
                <label for="userName" class="form-label">Name</label>
                <input type="text" class="form-control" id="userName" name="userName" value="<%= userInfo[1] %>">
            </div>
            <div class="mb-3">
                <label for="userEmail" class="form-label">Email</label>
                <input type="email" class="form-control" id="userEmail" name="userEmail" value="<%= userInfo[2] %>">
            </div>
            <div class="mb-3">
                <label for="userPassword" class="form-label">Password</label>
                <input type="password" class="form-control" id="userPassword" name="userPassword" value="<%= userInfo[3] %>">
            </div>
            <div class="d-flex">
        		<button type="submit" class="btn btn-primary me-2">수정하기</button>
        		<a href="../JSP/logoutAction.jsp" class="btn btn-danger">로그아웃</a> <!-- 수정한 부분 -->
    		</div>
        </form>
        <%
                } else {
        %>
            <p>사용자 정보를 찾을 수 없습니다.</p>
        <%
                }
            } else {
        %>
            <p>로그인하지 않았습니다. <a href="../html/login.html">로그인</a> 하세요.</p>
        <%
            }
        %>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
