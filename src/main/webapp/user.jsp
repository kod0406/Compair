<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="DAO.UserDAO" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="java.io.PrintWriter" %>
<%
    if (session == null || session.getAttribute("uid") == null) {
        response.sendRedirect("login.jsp");  // 로그인되지 않은 경우 login.jsp로 리다이렉션
        return;
    }

    String uid = (String) session.getAttribute("uid");  // 세션에서 사용자 ID 가져오기
    UserDAO userDAO = new UserDAO();
    String[] userInfo = userDAO.getUserInfoById(uid);  // 사용자 정보 가져오기
    String userId = userInfo[0];
    String userName = userInfo[1];
    String userMail = userInfo[2];
    String userPass = userInfo[3];
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>User Information</title>
</head>
<body>
    <h1>사용자 정보</h1>
    <form action="userupdate.jsp" method="post">
        <div>
            <label for="id"><strong>아이디:</strong></label>
            <input type="text" id="id" name="uid" value="<%= userId %>" placeholder="아이디를 입력하세요" required  readonly />
        </div>
        <div>
            <label for="name"><strong>이름:</strong></label>
            <input type="text" id="name" name="username" value="<%= userName %>" placeholder="이름을 입력하세요" required />
        </div>
        <div>
            <label for="mail"><strong>이메일:</strong></label>
            <input type="email" id="mail" name="usermail" value="<%= userMail %>" placeholder="이메일을 입력하세요" required />
        </div>
        <div>
            <label for="password"><strong>비밀번호:</strong></label>
            <input type="password" id="password" name="userpass" value="<%= userPass %>" placeholder="비밀번호를 입력하세요" required />
        </div>
        <div>
            <input type="submit" value="수정 완료" />
        </div>
    </form>
    <a href="logout.jsp">로그아웃</a>
    <form action="withdraw.jsp" method="post" onsubmit="return confirm('정말로 탈퇴하시겠습니까?');">
    <input type="hidden" name="uid" value="<%= userId %>" />
    <input type="submit" value="회원탈퇴" />
</form>
</body>
</html>
