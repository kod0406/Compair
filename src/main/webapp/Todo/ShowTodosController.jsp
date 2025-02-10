<%@page import="org.json.simple.JSONArray"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="DAO.TodoDAO"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Arrays"%>
<%@ page contentType="application/json; charset=UTF-8" %>
<%
try {
    int serverCode = Integer.parseInt(request.getParameter("server_code"));
    String userId = (String) session.getAttribute("uid");
    if (userId == null) {
        userId = request.getParameter("uid");
    }

    TodoDAO dao = new TodoDAO();
    List<Map<String, Object>> todos = dao.todoGetgroup(serverCode, userId);

    JSONArray jsonArr = new JSONArray();
    for (Map<String, Object> todo : todos) {
        JSONObject obj = new JSONObject();
        obj.put("todo_code", todo.get("todo_code"));
        obj.put("title", todo.get("title"));
        obj.put("writer", todo.get("writer"));
        obj.put("check", todo.get("check"));
        obj.put("post_date", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(todo.get("post_date")));
        obj.put("tags", Arrays.asList((String[]) todo.get("tag")));
        jsonArr.add(obj);
    }
    out.print(jsonArr.toString());

} catch (Exception e) {
    response.setStatus(500);
    out.print("{ \"error\": \"목록 조회 실패\" }");
    e.printStackTrace();
}
%>
