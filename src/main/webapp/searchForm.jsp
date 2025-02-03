<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>사용자 검색</title>
</head>
<body>
    <h2>이메일로 사용자 검색</h2>
    <form action="searchResult.jsp" method="post">
        이메일: <input type="text" name="user_mail">
        <input type="submit" value="검색">
    </form>
</body>
</html>