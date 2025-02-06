<%@ page contentType="text/html" pageEncoding="utf-8" %>
<%@ page import="DAO.*" %>
<%@ page import="java.util.*" %>
<%@ page import="org.json.simple.JSONArray" %>
<%@ page import="org.json.simple.JSONObject" %>
<%
    String serverList = request.getParameter("serverList");
	ServerDAO sd = new ServerDAO();
	String str = sd.checkServer(serverList);
	System.out.println("str은 ? " + str);
	StringTokenizer st = new StringTokenizer(str, " ");
	JSONObject obj = new JSONObject();
	System.out.println("토큰 개수 : " + st.countTokens());
	int num = st.countTokens();
	for(int i=0; i< num; i++){
		obj.put(i, st.nextToken());
		System.out.println("토큰 키 벨류 + " + i + " " + obj.get(i));
	}
	
	String rvalue = obj.toString();
	System.out.println(rvalue);
	out.print(rvalue);
%>