<%@ page contentType="text/html" pageEncoding="utf-8" %>
<%@ page import="DAO.*" %>
<%
    String POST_DATE = request.getParameter("POST_DATE");
    out.print((new FeedDAO()).calendarGetGroup(POST_DATE));
%>