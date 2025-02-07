<%@ page contentType="text/html" pageEncoding="utf-8" %>
<% 
	session.removeAttribute("serverCode");
	session.removeAttribute("nowScreen");
	String ServerCode = request.getParameter("serverCode");
	String nowScreen = request.getParameter("noewScreen");
    session.setAttribute("serverCode", ServerCode);
    session.setAttribute("nowScreen", nowScreen);
	String rvalue = ServerCode + " " + nowScreen;
	out.print(rvalue);
%>