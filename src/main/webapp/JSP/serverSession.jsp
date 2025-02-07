<%@ page contentType="text/html" pageEncoding="utf-8" %>
<% 
	String serverSession = (String) session.getAttribute("serverSession");
	System.out.println("흐음111 " + serverSession);
    out.println(serverSession);
%>