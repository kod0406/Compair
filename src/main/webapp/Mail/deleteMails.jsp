<%@page import="DAO.MailDAO"%>
<%@page import="java.util.Arrays"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%
    String userID = (String) session.getAttribute("uid");
    String[] mailCodeArr = request.getParameterValues("mailCodes");
    String[] serverCodeArr = request.getParameterValues("serverCodes");

    if(userID != null && mailCodeArr != null && serverCodeArr != null) {
        int[] mailCodes = Arrays.stream(mailCodeArr).mapToInt(Integer::parseInt).toArray();
        int[] serverCodes = Arrays.stream(serverCodeArr).mapToInt(Integer::parseInt).toArray();
        
        MailDAO dao = new MailDAO();
        boolean result = dao.deleteMails(userID, mailCodes, serverCodes);
        
        if(result) {
            response.sendRedirect("mailList.jsp");
        } else {
            out.println("<script>alert('삭제 실패!'); history.back();</script>");
        }
    } else {
        response.sendRedirect("main.jsp");
    }
%>
