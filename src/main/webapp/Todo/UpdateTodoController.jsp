<%@page import="DAO.TodoDAO"%>
<%@page import="DAO.UserDAO"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@ page contentType="application/json; charset=UTF-8"%>
<%@page import="org.json.simple.JSONObject"%>
<%
try {
	// 1. 파라미터
	String todoCodeStr = request.getParameter("todo_code");
	String serverCodeStr = request.getParameter("server_code");
	String newTitle = request.getParameter("new_title");
	String newContent = request.getParameter("new_content"); // 내용 파라미터 추가
	String newTag = request.getParameter("new_tag");
	String userId = (String) session.getAttribute("uid");
	if (userId == null) {
		userId = request.getParameter("uid");
	} // Postman 디버깅용

	// Debugging: Print parameters
	System.out.println("todoCodeStr: " + todoCodeStr);
	System.out.println("serverCodeStr: " + serverCodeStr);
	System.out.println("newTitle: " + newTitle);
	System.out.println("newContent: " + newContent);
	System.out.println("newTag: " + newTag);
	System.out.println("userId: " + userId);

	if (todoCodeStr == null || serverCodeStr == null || newTitle == null || newContent == null || newTag == null
	|| userId == null) {
		throw new NullPointerException("One or more request parameters are null");
	}

	int todoCode = Integer.parseInt(todoCodeStr);
	int serverCode = Integer.parseInt(serverCodeStr);

	// 2. UID 존재 여부 확인
	UserDAO userDao = new UserDAO();
	String[] tagArray = newTag.split(",");
	for (String tag : tagArray) {
		if (!userDao.isIdExist(tag)) {
	out.print("{ \"error\": \"해당 UID 없음: " + tag + "\" }");
	return;
		} else if (!userDao.isUserInServer(tag, serverCode)) {
	out.print("{ \"error\": \"해당 UID는 서버에 속해있지 않음: " + tag + "\" }");
	return;
		}
	}

	// 3. DAO 호출
	TodoDAO dao = new TodoDAO();
	Map<String, Object> resultMap = dao.updateTodo(todoCode, serverCode, newTitle, newContent, newTag, userId);

	if (resultMap == null) {
		throw new NullPointerException("resultMap is null");
	}

	boolean success = (boolean) resultMap.get("success");
	List<String> duplicateTags = (List<String>) resultMap.get("duplicateTags");

	// 4. JSON 응답 생성
	JSONObject responseJson = new JSONObject();
	if (duplicateTags != null && !duplicateTags.isEmpty()) {
		responseJson.put("duplicate_tags", duplicateTags); // 중복된 태그 반환
	} else {
		responseJson.put("success", success);
	}

	out.print(responseJson.toJSONString());

} catch (Exception e) {
	response.setStatus(500);
	out.print("{ \"error\": \"수정 실패: " + e.getMessage() + "\" }");
	e.printStackTrace(); // Debugging: Print stack trace
}
%>
