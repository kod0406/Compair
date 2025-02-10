
<%@page import="DAO.MailDAO"%>
<%@page import="user.Mail"%>
<%@page import="java.util.List"%>
<%@page import="org.json.simple.JSONArray"%>
<%@page import="org.json.simple.JSONObject"%>
<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    request.setCharacterEncoding("utf-8");
    String userID = null;
    JSONArray jsonArray = new JSONArray();

    // 세션에서 uid 가져오기
    if (session.getAttribute("uid") != null) {
        userID = (String) session.getAttribute("uid");
    }

    try {
        MailDAO mailDAO = new MailDAO();

        // 요청에서 server_code 가져오기
        String serverCodeParam = request.getParameter("server_code");
        if (serverCodeParam == null || serverCodeParam.isEmpty()) {
            throw new IllegalArgumentException("server_code 파라미터가 필요합니다.");
        }
        int serverCode = Integer.parseInt(serverCodeParam);

        // 해당 사용자와 서버에 해당하는 송신 메일 목록 가져오기
        List<Mail> mailList = mailDAO.getSentMailList(userID, serverCode);
        for (Mail mail : mailList) {
            JSONObject mailObj = new JSONObject();
            mailObj.put("mail_code", mail.getMail_code());
            mailObj.put("mail_title", mail.getMail_title());
            mailObj.put("writer", mail.getWriter());
            mailObj.put("receiver", mail.getReceiver());
            mailObj.put("post_date", mail.getPost_date().toString());
            mailObj.put("server_code", mail.getServer_code());
            jsonArray.add(mailObj);
        }

        // JSON 배열 응답
        out.print(jsonArray.toJSONString());
    } catch (Exception e) {
        JSONObject errorObj = new JSONObject();
        errorObj.put("error", "서버 오류가 발생했습니다: " + e.getMessage());
        out.print(errorObj.toJSONString());
    }
%>
