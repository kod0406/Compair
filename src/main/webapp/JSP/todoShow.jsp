<%@ page contentType="text/html" pageEncoding="utf-8" %>
<%@ page import="DAO.*" %>
<%
	String serverSession = (String) request.getParameter("recentServerCode");
	System.out.println(serverSession);
    out.print((new TodoDAO()).todoShowGetgroup(serverSession));
%>