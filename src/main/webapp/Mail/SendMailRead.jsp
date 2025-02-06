<%@page import="DAO.MailDAO"%>
<%@page import="DAO.ServerDAO"%>
<%@page import="user.Mail"%>
<%@page import="java.util.List"%>
<%@page import="java.io.PrintWriter"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>보낸 메일함</title>
<style>
    table {
        width: 100%;
        border-collapse: collapse;
    }
    th, td {
        border: 1px solid #ddd;
        padding: 8px;
        text-align: left;
    }
    th {
        background-color: #f2f2f2;
    }
    tr:hover {
        background-color: #f5f5f5;
    }
</style>
</head>
<body>
<h1>보낸 메일함</h1>
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
        script.println("alert('로그인 필수.');");
        script.println("location.href = 'main.jsp';");
        script.println("</script>");
    } else {
        try {
            MailDAO mailDAO = new MailDAO();
            ServerDAO serverDAO = new ServerDAO();
            
            int serverCode = serverDAO.getUserServerCode(userID);
            
            if (serverCode == -1) {
%>
                <p>서버 정보를 불러올 수 없습니다.</p>
<%
            } else {
                List<Mail> mailList = mailDAO.getSentMailList(userID, serverCode);
                
                if (!mailList.isEmpty()) {
%>
                    <form action="deleteMails.jsp" method="post">
                        <table>
                            <thead>
                                <tr>
                                    <th>선택</th>
                                    <th>제목</th>
                                    <th>받는 사람</th>
                                    <th>보낸 날짜</th>
                                    <th>내용 보기</th>
                                </tr>
                            </thead>
                            <tbody>
<%
                                for (Mail mail : mailList) {
%>
                                <tr>
                                    <td>
                                        <input type="checkbox" 
                                               name="mailCodes" 
                                               value="<%=mail.getMail_code()%>">
                                        <input type="hidden" 
                                               name="serverCodes" 
                                               value="<%=mail.getServer_code()%>">
                                    </td>
                                    <td><%=mail.getMail_title()%></td>
                                    <td><%=mail.getReceiver()%></td>
                                    <td><%=mail.getPost_date()%></td>
                                    <td>
                                       <a href="MailDispatcher.jsp?mail_code=<%=mail.getMail_code()%>&server_code=<%=mail.getServer_code()%>">보기</a>
                                    </td>
                                </tr>
<%
                                }
%>
                            </tbody>
                        </table>
                        <input type="submit" value="선택 삭제">
                    </form>
<%
                } else {
%>
                    <p>보낸 메일이 없습니다.</p>
<%
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
%>
            <p>메일 목록을 불러오는 중 오류가 발생했습니다.</p>
<%
        }
    }
%>

<hr>
<input type="button" value="메일 작성하기" onclick="location.href='mailWriteForm.jsp'">
<input type="button" value="뒤로 가기" onclick="history.back()">
</body>
</html>
