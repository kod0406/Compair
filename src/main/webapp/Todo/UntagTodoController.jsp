<%@page import="DAO.TodoDAO"%>
<%@ page contentType="application/json; charset=UTF-8" %>
<%
try {
    // 1. 파라미터 처리 (배열 형태)
    String currentUserId = (String) session.getAttribute("uid");
    if (currentUserId == null) {
    	currentUserId = request.getParameter("uid");
    }//Postman 디버깅용
    String[] todoCodeArr = request.getParameterValues("todo_code[]");
    String[] serverCodeArr = request.getParameterValues("server_code[]");
    
    // 2. 배열 변환
    int[] todoCodes = new int[todoCodeArr.length];
    int[] serverCodes = new int[serverCodeArr.length];
    for(int i=0; i<todoCodeArr.length; i++) {
        todoCodes[i] = Integer.parseInt(todoCodeArr[i]);
        serverCodes[i] = Integer.parseInt(serverCodeArr[i]);
    }

    // 3. DAO 호출
    TodoDAO dao = new TodoDAO();
    boolean success = dao.UntagTodos(currentUserId, todoCodes, serverCodes);
    
    // 4. 결과 응답
    out.print("{ \"success\": " + success + " }");

} catch (Exception e) {
    response.setStatus(500);
    out.print("{ \"error\": \"언태그 실패: " + e.getMessage() + "\" }");
}
%>
