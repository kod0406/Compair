<%@ page contentType="text/html" pageEncoding="utf-8" %>
<% 
	String ServerCode = request.getParameter("serverCode");
	String nowScreen = request.getParameter("nowScreen");
    session.setAttribute("serverCode", ServerCode);
    session.setAttribute("nowScreen", nowScreen);
	String rvalue = ServerCode + " " + nowScreen;
	out.print(rvalue);
%>