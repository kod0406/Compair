<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "DAO.UserDAO" %>
<%@ page import = "user.User" %>  <!-- user 패키지의 User 클래스 임포트 추가 -->
<%@ page import ="java.io.PrintWriter" %>
<% request.setCharacterEncoding("UTF-8"); %>
<jsp:useBean id="user" class="user.User" scope="page" />
<jsp:setProperty name="user" property="uid" />
<jsp:setProperty name="user" property="upass" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login</title>
</head>
<body>
	<%
	UserDAO userDAO = new UserDAO();
	int result = userDAO.login(user.getUid(), user.getUpass()); 
	if (result == 1) {
        session.setAttribute("uid", user.getUid()); // 세션에 사용자 ID 저장
        response.sendRedirect("index.jsp"); // 로그인 성공 시 user.html로 이동
	} else {
        PrintWriter script = response.getWriter();
        script.println("<script>");
        if (result == 0) {
            script.println("alert('비밀번호가 틀렸습니다.');");
        } else if (result == -1) {
            script.println("alert('아이디가 없습니다.');");
        } else {
            script.println("alert('데이터베이스 오류가 발생했습니다.');");
        }
        script.println("history.back();");
        script.println("</script>");
    }
	%>
</body>
</html>