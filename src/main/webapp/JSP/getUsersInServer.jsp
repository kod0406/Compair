
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="DAO.ServerDAO" %>
<%@ page import="user.User" %>
<%
    String serverCodeStr = request.getParameter("serverCode");
    int serverCode = Integer.parseInt(serverCodeStr);

    ServerDAO serverDAO = new ServerDAO();
    List<User> usersInServer = serverDAO.getUsersInServer(serverCode);

    StringBuilder json = new StringBuilder("[");
    for (int i = 0; i < usersInServer.size(); i++) {
        User user = usersInServer.get(i);
        json.append("{\"uid\":\"").append(user.getUid()).append("\",")
            .append("\"user_id\":\"").append(user.getUser_id()).append("\"}");
        if (i < usersInServer.size() - 1) {
            json.append(",");
        }
    }
    json.append("]");
    response.setContentType("application/json");
    response.getWriter().write(json.toString());
%>
