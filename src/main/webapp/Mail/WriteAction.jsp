<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="DAO.MailDAO" %>
<%@ page import="DAO.ServerDAO" %>
<%@ page import="user.Mail" %>
<%@ page import="java.io.PrintWriter" %>
<% request.setCharacterEncoding("UTF-8"); %>
<jsp:useBean id="mail" class="user.Mail" scope="page" />
<jsp:setProperty name="mail" property="mail_title" param="subject"/>
<jsp:setProperty name="mail" property="todoContent" param="content"/>
<jsp:setProperty name="mail" property="receiver"/>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>메일 전송</title>
</head>
<body>
    <%
    String userID = null;
    if(session.getAttribute("uid") != null) {
        userID = (String)session.getAttribute("uid");
    }
    
    // 로그인 체크
    if(userID == null) {
        PrintWriter script = response.getWriter();
        script.println("<script>");
        script.println("alert('로그인이 필요합니다.')");
        script.println("location.href = 'main.jsp'");
        script.println("</script>");
    } else {
        if(mail.getMail_title() == null || mail.getTodoContent() == null || 
           mail.getReceiver() == null || mail.getMail_title().equals("") || 
           mail.getTodoContent().equals("") || mail.getReceiver().equals("")) {
            PrintWriter script = response.getWriter();
            script.println("<script>");
            script.println("alert('입력이 안 된 사항이 있습니다.')");
            script.println("history.back()");
            script.println("</script>");
        } else {
            MailDAO mailDAO = new MailDAO();
            ServerDAO serverDAO = new ServerDAO();
            
            // 현재 사용자의 서버 코드 가져오기
            int serverCode = serverDAO.getUserServerCode(userID);
            
            // 메일 객체 설정
            mail.setServer_code(serverCode);
            mail.setWriter(userID);
            
            // 메일 전송 시도
            boolean result = mailDAO.insertMail(mail);
            
            if(result) {
                PrintWriter script = response.getWriter();
                script.println("<script>");
                script.println("alert('메일이 전송되었습니다.')");
                script.println("location.href = 'SendMailRead.jsp");
                script.println("</script>");
            } else {
                PrintWriter script = response.getWriter();
                script.println("<script>");
                script.println("alert('메일 전송에 실패했습니다.')");
                script.println("history.back()");
                script.println("</script>");
            }
        }
    }
    %>
</body>
</html>
