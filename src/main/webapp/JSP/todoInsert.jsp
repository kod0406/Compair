<%@ page contentType="text/html" pageEncoding="utf-8" %>
<%@ page import="DAO.*" %>
<%
    String POST_DATE = request.getParameter("POST_DATE");
	//세션 변수 추가 후 매개변수로 다시 전달
	int ServerCode = Integer.parseInt(request.getParameter("ServerCode"));
	String Date = request.getParameter("thisPageDate");
	String todoInput = request.getParameter("todoInput");
	String todoWriter = (String)session.getAttribute("uid");
	System.out.println("Date 출력" + Date);
    out.print((new TodoDAO()).todoInsert(ServerCode, todoInput, todoWriter, Date));
    System.out.println("calendarGet의 서버 세션 드록 :" + session.getAttribute("serverSession"));
%>