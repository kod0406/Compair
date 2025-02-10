<%@page import="org.json.simple.JSONArray"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="DAO.TodoDAO"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Arrays"%>
<%@ page contentType="application/json; charset=UTF-8" %>
<%
try {
    // 1. 파라미터
    int serverCode = Integer.parseInt(request.getParameter("server_code"));
    String userId = (String) session.getAttribute("uid");

    // Debugging: Print parameters
    System.out.println("serverCode: " + serverCode);
    if (userId == null) {
        userId = request.getParameter("uid");
    }//Postman 디버깅용
    System.out.println("userId: " + userId);
	
    // 2. DAO 호출
    TodoDAO dao = new TodoDAO();
    List<Map<String, Object>> todos = dao.todoGetgroup(serverCode, userId);

    // Debugging: Print size of the todo list
    System.out.println("Number of todos: " + todos.size());

    // 3. JSON 변환
    JSONArray jsonArr = new JSONArray();
    for (Map<String, Object> todo : todos) {
        JSONObject obj = new JSONObject();
        // 기본 정보
        obj.put("todo_code", todo.get("todo_code"));
        obj.put("title", todo.get("title"));
        obj.put("writer", todo.get("writer"));
        obj.put("check", todo.get("check"));

        // Debugging: Print basic info
        System.out.println("todo_code: " + todo.get("todo_code"));
        System.out.println("title: " + todo.get("title"));
        System.out.println("writer: " + todo.get("writer"));
        System.out.println("check: " + todo.get("check"));

        // 날짜 포맷팅 (yyyy-MM-dd HH:mm:ss)
        java.sql.Date postDate = (java.sql.Date) todo.get("post_date");
        obj.put("post_date", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(postDate));

        // Debugging: Print post date
        System.out.println("post_date: " + postDate);

        // 태그 배열 처리
        String[] tags = (String[]) todo.get("tag");
        JSONArray tagArray = new JSONArray();
        for (String tag : tags) {
            tagArray.add(tag);
        }
        obj.put("tags", tagArray);

        // Debugging: Print tags
        System.out.println("tags: " + Arrays.toString(tags));

        // 상세 내용
        obj.put("content", todo.get("content"));

        // Debugging: Print content
        System.out.println("content: " + todo.get("content"));

        // 콘솔에 출력
        System.out.println(obj.toJSONString());

        jsonArr.add(obj);
    }
    out.print(jsonArr.toString());

} catch (Exception e) {
    response.setStatus(500);
    out.print("{ \"error\": \"목록 조회 실패\" }");
    e.printStackTrace(); // Debugging: Print stack trace
}
%>
