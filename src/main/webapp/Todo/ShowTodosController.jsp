<%@page import="org.json.simple.JSONArray"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="DAO.TodoDAO"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Arrays"%>
<%@ page contentType="application/json; charset=UTF-8" %>

<%
// 1. recentServerCode 파라미터 NULL 체크
String ServerCode = request.getParameter("recentServerCode");
if(ServerCode == null) {
    JSONObject error = new JSONObject();
    error.put("error", "recentServerCode parameter required");
    out.print(error);
    return;
}

// 2. 세션 유저 ID NULL 체크
String userId = (String) session.getAttribute("uid");
if(userId == null) {
    JSONObject error = new JSONObject();
    error.put("error", "User not authenticated");
    out.print(error);
    return;
}

// 3. 서버 코드 파싱
int server;
try {
    server = Integer.parseInt(ServerCode);
} catch(NumberFormatException e) {
    JSONObject error = new JSONObject();
    error.put("error", "Invalid server code format");
    out.print(error);
    return;
}

// 4. 데이터 조회 및 응답
TodoDAO dao = new TodoDAO();
List<Map<String, Object>> todos = dao.todoGetgroup(server, userId);

JSONArray jsonArr = new JSONArray();
for(Map<String, Object> todo : todos) {
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
%>
