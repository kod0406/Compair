<%@ page contentType="text/html" pageEncoding="utf-8" %>
<%@ page import="DAO.*" %>
<%
    String maxNo = request.getParameter("maxNo");
	String sc = request.getParameter("recentServerCode");
	System.out.println(sc);
    out.print((new FeedDAO()).getGroup(maxNo, sc));
%>