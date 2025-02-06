<%@page import="DAO.MailDAO"%>
<%@page import="user.Mail"%>
<%@page import="java.io.PrintWriter"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    request.setCharacterEncoding("utf-8");
    String userID = null;
    if (session.getAttribute("uid") != null) {
        userID = (String) session.getAttribute("uid");
    }

    if (userID == null) {
        response.sendRedirect("main.jsp");
        return;
    }

    String strMailCode = request.getParameter("mail_code");
    String strServerCode = request.getParameter("server_code");

    try {
        MailDAO mailDAO = new MailDAO();
        int mailCode = Integer.parseInt(strMailCode);
        int serverCode = Integer.parseInt(strServerCode);
        Mail mail = mailDAO.getMail(mailCode, serverCode);

        if (mail != null) {
            // 메일 데이터를 request에 저장
            request.setAttribute("mail", mail);
            // mailRead.jsp로 포워딩
            request.getRequestDispatcher("ReadMail.jsp").forward(request, response);
        } else {
            PrintWriter script = response.getWriter();
            script.println("<script>");
            script.println("alert('메일이 존재하지 않습니다.');");
            script.println("history.back();");
            script.println("</script>");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
%>
