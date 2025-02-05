<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="DAO.UserDAO" %>
<%
    request.setCharacterEncoding("utf-8");

    // HTML 폼에서 전달받은 파라미터
    String uid = request.getParameter("id");
    String upass = request.getParameter("password");
    String confirmPass = request.getParameter("confirm_password");
    String email = request.getParameter("mail");
    String name = request.getParameter("name");

    UserDAO user = new UserDAO();
	
	 
    // 중복 체크
    if (user.isIdExist(uid)) {
    	%>
    	<script>
    		alert("id가 이미 존재");
    		history.back();
    	</script>
    <% 
    } else if (user.isEmailExist(email)) {
    	%>
    	<script>
    		alert("email이 이미 존재");
    		history.back();
    	</script>
    <% 
    } else if (!upass.equals(confirmPass)) {  // 비밀번호와 확인값이 일치하지 않으면
    	%>
    	<script>
    		alert("비밀번호가 불일치");
    		history.back();
    	</script>
   <% 
    } else {
        // 회원가입 진행
        if (user.insert(uid, upass, email, name)) {
        	%>
        	<script>
            	alert("회원가입 성공");
            	window.location.href = "../html/login.html";
            </script>
    <% 
        } else {
            out.print("ER");
        }
    }
%>
