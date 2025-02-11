
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="DAO.ServerDAO" %>
<%@ page import="java.io.PrintWriter" %>
<% request.setCharacterEncoding("UTF-8"); %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Server Sign Up</title>
</head>
<body>
<%
    String uid = (String) session.getAttribute("uid");
    String scodeStr = request.getParameter("scode");
    boolean isNumeric = scodeStr != null && scodeStr.matches("\\d+");
    int scode = 0;
    boolean success = false;

    if (isNumeric) {
        scode = Integer.parseInt(scodeStr);
        ServerDAO serverDao = new ServerDAO();

        System.out.println("User ID: " + uid);
        System.out.println("Server Code: " + scode);

        try {
            // Attempt to insert the server
            success = serverDao.insertServer(uid, scode);
            System.out.println("Insert server success: " + success);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
%>

<script>
    let isNumeric = <%= isNumeric %>;
    let success = <%= success %>;

    if (!isNumeric) {
        alert('숫자를 입력해주세요.');
        history.back();
    } else {
        console.log("Success: " + success);

        if (success) {
            alert('서버가 성공적으로 생성되었습니다.');
            window.location.href = "../html/main.html";
        } else {
            alert('서버 생성에 실패했습니다.');
            history.back();
        }
    }
</script>

</body>
</html>
