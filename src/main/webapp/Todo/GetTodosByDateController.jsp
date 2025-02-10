<%@page import="org.json.simple.JSONArray"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="DAO.TodoDAO"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Arrays"%>
<%@ page contentType="application/json; charset=UTF-8" %>
<%
try {
    int serverCode = Integer.parseInt(request.getParameter("server_code"));
    String postDate = request.getParameter("post_date");
    String userId = request.getParameter("uid");

    if (!postDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
        response.setStatus(400);
        out.print("{ \"error\": \"날짜 형식 오류: YYYY-MM-DD 필요\" }");
        return;
    }

    TodoDAO dao = new TodoDAO();
    List<Map<String, Object>> todos = dao.getTodosByPostDateAndUser(serverCode, postDate, userId);

    JSONArray jsonArr = new JSONArray();
    for (Map<String, Object> todo : todos) {
        JSONObject obj = new JSONObject();
        obj.put("todo_code", todo.get("todo_code"));
        obj.put("title", todo.get("title"));
        obj.put("writer", todo.get("writer"));
        obj.put("check", todo.get("check"));
        obj.put("tag", Arrays.asList((String[]) todo.get("tag"))); // Convert array to list
        obj.put("post_date", todo.get("post_date"));
        jsonArr.add(obj);
    }
    out.print(jsonArr.toString());

} catch (Exception e) {
    response.setStatus(500);
    out.print("{ \"error\": \"데이터 조회 실패: " + e.getMessage() + "\" }");
}
%>
