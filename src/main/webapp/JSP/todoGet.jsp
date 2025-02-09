<%@ page contentType="text/html" pageEncoding="utf-8" %>
<%@ page import="DAO.*" %>
<%
    String POST_DATE = request.getParameter("POST_DATE");
	//세션 변수 추가 후 매개변수로 다시 전달
	String serverSession = (String) request.getParameter("ServerCode");
	System.out.println(serverSession);
    out.print((new TodoDAO()).todoGetgroup(POST_DATE, serverSession));
%>