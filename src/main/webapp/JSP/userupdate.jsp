<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="DAO.UserDAO" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>

<html lang="ko">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>정보 수정 완료</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet" />
</head>
<body>
    <div class="container mt-5">
        <h2></h2>
        <%
            session = request.getSession(false);
            if (session != null && session.getAttribute("uid") != null) {
                String userId = (String) session.getAttribute("uid");
                String userName = request.getParameter("userName");
                String userEmail = request.getParameter("userEmail");
                String userPassword = request.getParameter("userPassword");

                UserDAO userDAO = new UserDAO();
                try {
                    boolean isUpdated = userDAO.updateUserInfo(userId, userName, userEmail, userPassword);
                    
                    if (isUpdated) {
            %>
                        <script>
                            alert("사용자 정보가 성공적으로 업데이트되었습니다.");
                            window.location.href = "../html/main.html"; 
                        </script>
            <%
                    } else {
            %>
                        <p>사용자 정보 업데이트에 실패했습니다. 다시 시도해주세요.</p>
            <%
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    out.println("<p>오류가 발생했습니다. 다시 시도해주세요.</p>");
                }
            } else {
            %>
                <p>로그인하지 않았습니다. <a href="../html/login.jsp">로그인</a> 하세요.</p>
            <%
            }
        %>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
