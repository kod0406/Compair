<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import = "java.util.ArrayList"%>
<%@ page import = "DAO.FeedObj"%>
<%@ page import = "DAO.FeedDAO"%>    
<%
	ArrayList<FeedObj> feeds = (new FeedDAO()).getList();
	String str = "<table align=center>";
	for(FeedObj feed : feeds){
		String img = feed.getImages(), imgstr = "";
		if(img != null) imgstr = "<img src= 'images/" + img + "' width=240>";
		
		str += "<tr><td colspan=2><hr><td></tr>";
		str += "<tr>";
		str += "<td><small>" + feed.getId() + "</small></td>";
		str += "<td><small>&nbsp;(" + feed.getContent() + ")</small></td>";
		str += "</tr>";
		str += "<td><td colspan=2>" + imgstr + "</td></tr>";
		str += "<td><td colspan=2>" + feed.getContent() + "</td></tr>";
	}
	str += "</table>";
	out.print(str);

%>