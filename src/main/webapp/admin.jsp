<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="DAO.UserDAO"%>
<%@ page import="java.util.*"%>
<%
// 관리자 권한 확인
String adminId = (String) session.getAttribute("uid");
UserDAO userDAO = new UserDAO();
if (adminId == null || !userDAO.isAdmin(adminId)) {
%><script>
	alert("관리자 권한이 없습니다.");
</script>
<%
response.sendRedirect("main.jsp");
return;
}

List<Map<String, Object>> pendingUsers = userDAO.getPendingUsers();
List<Map<String, Object>> allUsers = userDAO.getAllUsers();
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>관리자 페이지</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
	// 승인/거절 기능
	function manageUser(action, userId) {
		if (confirm(userId + " 사용자를 " + (action === 'approve' ? "승인" : "거절")
				+ "하시겠습니까?")) {
			$.post("userManageAction.jsp", {
				action : action,
				userId : userId
			}, function(response) {
				location.reload();
			});
		}
	}

	// 선택 삭제 기능
	function deleteSelected() {
		const selected = [];
		$("input[name='selectedUsers']:checked").each(function() {
			selected.push($(this).val()); // 체크박스 값 수집
		});

		if (selected.length === 0) {
			alert("삭제할 사용자를 선택하세요.");
			return;
		}

		if (confirm(selected.length + "명을 삭제하시겠습니까?")) {
		    $.ajax({
		        url: "deleteUsers.jsp",
		        type: "POST",
		        traditional: true,
		        data: {
		            users: selected
		        },
		        success: function(response) {
		            const res = response.trim();
		            if (res === "success") {
		                location.reload();
		            } else if (res === "ERROR-AdminDelete") { // admin 차단 조건 추가
		                alert("관리자 계정(admin)은 삭제할 수 없습니다.");
		            } else {
		                alert("삭제 실패: " + res);
		            }
		        },
		        error: function(xhr) {
		            alert("오류: " + xhr.statusText);
		        }
		    });
		}
	}
</script>
</head>
<body>
	<h1>관리자 대시보드</h1>

	<!-- 승인 대기 목록 -->
	<h2>승인 대기 중인 사용자</h2>
	<table border="1">
		<tr>
			<th>ID</th>
			<th>이름</th>
			<th>이메일</th>
			<th>액션</th>
		</tr>
		<%
		for (Map<String, Object> user : pendingUsers) {
		%>
		<tr>
			<td><%=user.get("user_id")%></td>
			<td><%=user.get("user_name")%></td>
			<td><%=user.get("user_mail")%></td>
			<td>
				<button onclick="manageUser('approve', '<%=user.get("user_id")%>')">승인</button>
				<button onclick="manageUser('reject', '<%=user.get("user_id")%>')">거절</button>
			</td>
		</tr>
		<%
		}
		%>
	</table>

	<!-- 전체 사용자 관리 -->
	<h2>전체 사용자 목록</h2>
	<button onclick="deleteSelected()">선택 삭제</button>
	<table border="1">
		<tr>
			<th>선택</th>
			<th>ID</th>
			<th>이름</th>
			<th>이메일</th>
			<th>승인 상태</th>
		</tr>
		<%
		for (Map<String, Object> user : allUsers) {
		%>
		<tr>
			<td><input type="checkbox" name="selectedUsers"
				value="<%=user.get("user_id")%>"></td>
			<td><%=user.get("user_id")%></td>
			<td><%=user.get("user_name")%></td>
			<td><%=user.get("user_mail")%></td>
			<td><%=(Integer) user.get("APPROVED") == 1 ? "승인" : "대기"%></td>
		</tr>
		<%
		}
		%>
	</table>
</body>
</html>