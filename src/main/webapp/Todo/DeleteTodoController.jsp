<%@page import="DAO.TodoDAO"%>
<%@ page contentType="application/json; charset=UTF-8" %>
<%
try {
    // 1. 배열 파라미터 처리
    String[] todoCodes = request.getParameterValues("todo_code[]");
    String[] serverCodes = request.getParameterValues("server_code[]");
    
    int[] todoCodeArr = new int[todoCodes.length];
    int[] serverCodeArr = new int[serverCodes.length];
    for(int i=0; i<todoCodes.length; i++) {
        todoCodeArr[i] = Integer.parseInt(todoCodes[i]);
        serverCodeArr[i] = Integer.parseInt(serverCodes[i]);
    }

    // 2. DAO 호출
    TodoDAO dao = new TodoDAO();
    boolean success = dao.deleteTodos(todoCodeArr, serverCodeArr);
    
    out.print("{ \"success\": " + success + " }");

} catch (Exception e) {
    response.setStatus(500);
    out.print("{ \"error\": \"삭제 실패\" }");
}
%>
