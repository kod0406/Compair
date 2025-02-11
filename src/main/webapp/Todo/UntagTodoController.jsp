<%@page import="java.util.*"%>
<%@page import="DAO.TodoDAO"%>
<%@ page contentType="application/json; charset=UTF-8" %>
<%
try {
    String userId = (String) session.getAttribute("uid");
    if (userId == null) {
    	userId = request.getParameter("uid");
    }
    String[] todoCodeArr = request.getParameterValues("todo_code[]");
    String[] serverCodeArr = request.getParameterValues("server_code[]");

    int[] todoCodes = Arrays.stream(todoCodeArr).mapToInt(Integer::parseInt).toArray();
    int[] serverCodes = Arrays.stream(serverCodeArr).mapToInt(Integer::parseInt).toArray();

    TodoDAO dao = new TodoDAO();
    boolean success = dao.UntagTodos(userId, todoCodes, serverCodes);

    out.print("{ \"success\": " + success + " }");

} catch (Exception e) {
    response.setStatus(500);
    out.print("{ \"error\": \"언태그 실패: " + e.getMessage() + "\" }");
}
%>
