<%@page import="DAO.TodoDAO"%>
<%@ page contentType="application/json; charset=UTF-8" %>
<%
try {
    int todoCode = Integer.parseInt(request.getParameter("todo_code"));
    int serverCode = Integer.parseInt(request.getParameter("server_code"));
    String userId = (String) session.getAttribute("uid");
    if (userId == null) {
        userId = request.getParameter("uid");
    }

    TodoDAO dao = new TodoDAO();
    boolean success = dao.todoCheck(todoCode, serverCode, userId);

    out.print("{ \"success\": " + success + ", \"newState\": " + success + " }");

} catch (Exception e) {
    response.setStatus(500);
    out.print("{ \"error\": \"상태 변경 실패\" }");
}
%>
