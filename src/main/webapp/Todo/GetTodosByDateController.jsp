<%@page import="org.json.simple.JSONArray"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="DAO.TodoDAO"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@ page contentType="application/json; charset=UTF-8" %>
<%
try {
    // 1. 파라미터 검증
    int serverCode = Integer.parseInt(request.getParameter("server_code"));
    String postDate = request.getParameter("post_date"); // YYYY-MM-DD 형식
    
    // 2. 날짜 형식 검증
    if (!postDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
        response.setStatus(400);
        out.print("{ \"error\": \"날짜 형식 오류: YYYY-MM-DD 필요\" }");
        return;
    }

    // 3. DAO 호출
    TodoDAO dao = new TodoDAO();
    List<Map<String, Object>> todos = dao.getTodosByPostDate(serverCode, postDate);
    
    // 4. JSON 변환
    JSONArray jsonArr = new JSONArray();
    for (Map<String, Object> todo : todos) {
        JSONObject obj = new JSONObject();
        obj.put("todo_code", todo.get("todo_code"));
        obj.put("title", todo.get("title"));
        obj.put("writer", todo.get("writer"));
        obj.put("check", todo.get("check"));
        obj.put("tag", todo.get("tag"));
        obj.put("post_date", todo.get("post_date"));
        obj.put("content", todo.get("content"));
        obj.put("attachment", todo.get("attachment"));
        jsonArr.add(obj);
    }
    out.print(jsonArr.toString());
   
} catch (Exception e) {
    response.setStatus(500);
    out.print("{ \"error\": \"데이터 조회 실패: " + e.getMessage() + "\" }");
}
%>
