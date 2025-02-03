<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.io.PrintWriter"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<!-- 모바일 디바이스 대응을 위한 메타 태그 -->
<meta name="viewport" content="width=device-width, initial-scale=1">

<title>게시판!!</title>

<style>
/* 전체 배경색 */
body {
	background-color: lightblue;
	margin: 0;
	padding: 0;
	font-family: sans-serif;
}

/* 상단 네비게이션 바 */
.navbar {
	background-color: #333;
	overflow: hidden;
}

.navbar-header, .navbar-right {
	display: inline-block;
	vertical-align: middle;
}

.navbar a, .navbar button {
	color: #fff;
	text-decoration: none;
	display: inline-block;
	padding: 14px 16px;
	background: none;
	border: none;
	cursor: pointer;
}

.navbar a:hover, .navbar button:hover {
	background-color: #444;
}

/* 브랜드 로고 */
.navbar-brand {
	font-weight: bold;
	font-size: 1.2em;
}

/* 왼쪽 메뉴 */
.navbar-nav {
	margin: 0;
	padding: 0;
	display: inline-block;
}

.navbar-nav li {
	list-style: none;
	float: left;
}

/* 오른쪽 메뉴 */
.navbar-right {
	float: right;
}

/* 드롭다운 메뉴 */
.dropdown {
	position: relative;
	display: inline-block;
}

.dropdown .dropdown-menu {
	display: none;
	position: absolute;
	background-color: #fff;
	min-width: 120px;
	box-shadow: 0 2px 5px rgba(0, 0, 0, 0.25);
}

.dropdown:hover .dropdown-menu {
	display: block;
}

.dropdown-menu li {
	list-style: none;
}

.dropdown-menu li button {
	color: #333;
	background-color: #fff;
	width: 100%;
	text-align: left;
	border: none;
	padding: 10px;
	cursor: pointer;
}

.dropdown-menu li button:hover {
	background-color: #ddd;
}

/* 중앙 콘테이너 */
.container {
	max-width: 600px;
	margin: 30px auto;
	background-color: #fafafa;
	padding: 20px;
	border-radius: 8px;
}

/* 로그인 박스 */
.login-box {
	text-align: center;
}

.login-box h2 {
	margin-bottom: 20px;
}

.login-box .form-group {
	margin-bottom: 15px;
}

.login-box input[type="text"], .login-box input[type="password"] {
	width: 90%;
	padding: 10px;
	margin: 5px 0;
}

.login-box input[type="submit"] {
	width: 95%;
	background-color: #007bff;
	color: #fff;
	border: none;
	padding: 12px;
	cursor: pointer;
	margin-top: 10px;
}

.login-box input[type="submit"]:hover {
	background-color: #0056b3;
}
</style>
</head>
<body>
<%
	String userId = null;
	if (session.getAttribute("uid") != null) {
		userId = (String) session.getAttribute("uid");
	}
%>
<!-- 네비게이션 바 -->
<nav class="navbar">
	<div class="navbar-header">
		<a class="navbar-brand" href="main.jsp">JSP 게시판</a>
	</div>
	<ul class="navbar-nav">
		<li class="active"><a href="main.jsp">메뉴</a></li>
		<li><a href="login.jsp">게시판</a></li>
	</ul>
	<%
		// 1) 로그인 안 된 상태
		if (userId == null) {
	%>
	<div class="navbar-right">
		<div class="dropdown">
			<!-- 1. dropdown-toggle를 버튼으로 교체 -->
			<button type="button" class="dropdown-toggle">접속하기 ▼</button>
			<ul class="dropdown-menu">
				<!-- 2. 로그인/회원가입 버튼 -->
				<li>
					<button type="button" onclick="location.href='login.jsp'">로그인</button>
				</li>
				<li>
					<button type="button" onclick="location.href='NewFile.html'">회원가입</button>
				</li>
			</ul>
		</div>
	</div>
	<%
		// 2) 로그인 된 상태
		} else {
	%>
	<div class="navbar-right">
		<div class="dropdown">
			<button type="button" onclick="location.href='logoutAction.jsp'">로그아웃</button>
		</div>
	</div>
	<%
		}
	%>
</nav>

<!-- 중앙 컨텐츠 영역 -->
</body>
</html>
