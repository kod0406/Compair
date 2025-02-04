<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="DAO.UserDAO" %>
<%@ page import="java.util.*" %>
<%
    String[] users = request.getParameterValues("users"); 

    // [2] Null 및 빈 배열 체크 강화
    if (users == null || users.length == 0) {
        response.sendError(400, "선택된 사용자가 없습니다.");
        return;
    }
	
    for (String userId : users) {
        if ("admin".equals(userId)) { // admin ID가 포함된 경우
            out.print("ERROR-AdminDelete");
            return;
        }
    }
    
    UserDAO dao = new UserDAO();
    try {
        boolean success = dao.deleteUsers(users);
        if (success) {
            out.print("success");
        } else {
            response.sendError(500, "일부 사용자 삭제 실패");
        }
    } catch (Exception e) {
        e.printStackTrace();
        response.sendError(500, "서버 오류");
    }
%>
