<%@ page contentType="text/html" pageEncoding="utf-8" %>
<%@ page import="DAO.*" %>
<%	
	String good = request.getParameter("good");
    String maxNo = request.getParameter("maxNo");
	String sc = request.getParameter("recentServerCode");
	session.setAttribute("serverSession", sc);
	System.out.println(sc);
	System.out.println("feedGetGroup의 서버 세션 드록 :" + session.getAttribute("serverSession"));
    out.print((new FeedDAO()).getGroup(maxNo, sc, good));
%>