<%@ page contentType="text/html" pageEncoding="utf-8" %>
<%@ page import="DAO.*" %>
<%
	String serverSession = (String) request.getParameter("recentServerCode");
	System.out.println(serverSession);
	String uid = (String)session.getAttribute("uid");
	int numServerSession = Integer.parseInt(serverSession);
    out.print((new TodoDAO()).todoGetgroup(numServerSession,uid));
    
%>