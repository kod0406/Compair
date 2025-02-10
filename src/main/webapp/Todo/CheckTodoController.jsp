<%@page import="DAO.TodoDAO"%>
<%@ page contentType="application/json; charset=UTF-8" %>
<%
try {
    // 1. 파라미터
    int todoCode = Integer.parseInt(request.getParameter("todo_code"));
    int serverCode = Integer.parseInt(request.getParameter("server_code"));
    String userId = (String) session.getAttribute("uid");
    if (userId == null) {
    	userId = request.getParameter("uid");
    }//Postman 디버깅용

    // 2. DAO 호출
    TodoDAO dao = new TodoDAO();
    boolean success = dao.todoCheck(todoCode, serverCode, userId);
    
    // 3. 새로운 체크 상태 반환
    out.print("{ \"success\": " + success + ", \"newState\": " + success + " }");

} catch (Exception e) {
    response.setStatus(500);
    out.print("{ \"error\": \"상태 변경 실패\" }");
}
%>
