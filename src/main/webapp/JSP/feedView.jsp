<%@ page contentType="text/html" pageEncoding="utf-8" %>
<%@ page import="DAO.*" %>
<%
    String boardCode = request.getParameter("boardCode");
    out.print((new FeedDAO()).getOneFeed(boardCode));
%>