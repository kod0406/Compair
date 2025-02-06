<%@page import="java.net.URLEncoder"%>
<%@page import="DAO.MailDAO"%>
<%@page import="user.Mail"%>
<%@page import="java.io.PrintWriter"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>메일 읽기</title>
<style>
    table { width: 100%; border-collapse: collapse; }
    th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
    th { background-color: #f2f2f2; }
    tr:hover { background-color: #f5f5f5; }
</style>
</head>
<body>
<h1>메일 읽기</h1>
<hr>

<%
    request.setCharacterEncoding("utf-8");
    String userID = null;

    // 세션에서 uid 가져오기
    if (session.getAttribute("uid") != null) {
        userID = (String) session.getAttribute("uid");
    }

    // 로그인 여부 확인
    if (userID == null) {
        PrintWriter script = response.getWriter();
        script.println("<script>");
        script.println("alert('로그인 필수.')");
        script.println("location.href = 'main.jsp'");
        script.println("</script>");
    } else {
        try {
            Mail mail = (Mail) request.getAttribute("mail");
            String replyReceiver = "";

            if (mail != null) {
                // 답장 대상 결정 로직
                if (userID.equals(mail.getReceiver())) {
                    replyReceiver = mail.getWriter(); // 수신자 → 발신자에게 답장
                } else if (userID.equals(mail.getWriter())) {
                    replyReceiver = mail.getReceiver(); // 발신자 → 수신자에게 재전송
                }
                
                // URL 인코딩 (특수문자 처리)
                String encodedReceiver = URLEncoder.encode(replyReceiver, "UTF-8");
%>
                <table>
                    <tr><th>제목:</th><td><%=mail.getMail_title()%></td></tr>
                    <tr><th>보낸 사람:</th><td><%=mail.getWriter()%></td></tr>
                    <tr><th>받는 사람:</th><td><%=mail.getReceiver()%></td></tr>
                    <tr><th>보낸 날짜:</th><td><%=mail.getPost_date()%></td></tr>
                    <tr><th>내용:</th><td><%=mail.getTodoContent()%></td></tr>
                </table>
                <hr>
                <!-- 수정된 답장 버튼 -->
                <input type="button" value="답장하기" 
                       onclick="location.href='WriteMail.jsp?receiver=<%=encodedReceiver%>'">
                <input type="button" value="닫기" onclick="window.close()">
<%
            } else {
%>
                <p>메일을 찾을 수 없습니다.</p>
<%
            }
        } catch (Exception e) {
            e.printStackTrace();
%>
            <p>메일을 불러오는 중 오류가 발생했습니다.</p>
<%
        }
    }
%>

</body>
</html>
