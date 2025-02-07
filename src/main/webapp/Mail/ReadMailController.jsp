<%@page import="org.json.simple.JSONObject"%>
<%@page import="DAO.MailDAO"%>
<%@page import="user.Mail"%>
<%@ page contentType="application/json; charset=UTF-8" %>
<%
try {
    // 1. 파라미터 검증
    int mailCode = Integer.parseInt(request.getParameter("mail_code"));
    int serverCode = Integer.parseInt(request.getParameter("server_code"));
    
    // 2. 데이터 조회
    MailDAO mailDAO = new MailDAO();
    Mail mail = mailDAO.getMail(mailCode, serverCode);
    
    // 3. JSON 변환
    JSONObject json = new JSONObject();
    if (mail != null) {
        json.put("title", mail.getMail_title());
        json.put("writer", mail.getWriter());
        json.put("receiver", mail.getReceiver());
        json.put("content", mail.getTodoContent());
        json.put("post_date", mail.getPost_date().toString());
        json.put("attachment", mail.getAttachment());
    }
    out.print(json.toString());
    System.out.println(json);
} catch (Exception e) {
    response.setStatus(500);
    out.print("{ \"error\": \"서버 오류 발생\" }");
}
%>
