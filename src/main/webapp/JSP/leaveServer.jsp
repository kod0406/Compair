
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="DAO.ServerDAO" %>
<%@ page import="java.sql.SQLException" %>

<%
String uid = request.getParameter("uid");
String serverCodeStr = request.getParameter("serverCode");

if (serverCodeStr == null || serverCodeStr.trim().isEmpty() || "null".equals(serverCodeStr)) {
    %><script>alert('서버코드를 입력해주세요.'); history.back();</script><%
    return;
}

int serverCode = Integer.parseInt(serverCodeStr);

ServerDAO serverDAO = new ServerDAO();
boolean success = false;
try {
    success = serverDAO.leaveServer(uid, serverCode);
} catch (SQLException e) {
    e.printStackTrace();
}

if (success) {
%>
<script>
    alert("서버를 나갔습니다.");
</script>
<%
    response.sendRedirect("../html/main.html");
} else {
    out.println("<script>alert('서버를 나가는 중 오류가 발생했습니다.'); history.back();</script>");
}
%>
