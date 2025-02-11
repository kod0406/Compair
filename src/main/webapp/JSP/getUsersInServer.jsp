<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="DAO.ServerDAO" %>
<%@ page import="user.User" %>
<%
    String serverCodeStr = request.getParameter("serverCode");
    int serverCode = Integer.parseInt(serverCodeStr);

    ServerDAO serverDAO = new ServerDAO();
    String rvalue = serverDAO.getUsersInServer(serverCode);

    
    out.print(rvalue);
%>
