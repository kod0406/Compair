
<%@page import="DAO.MailDAO"%>
<%@page import="java.util.Arrays"%>
<%@page contentType="text/html; charset=UTF-8"%>
<%
    request.setCharacterEncoding("UTF-8");
    String userID = (String) session.getAttribute("uid");

    try {
        String mailCodeStr = request.getParameter("mailCodes");
        String serverCodeStr = request.getParameter("serverCodes");

        if(userID == null || mailCodeStr == null || serverCodeStr == null) {
            response.sendError(400, "Invalid parameters");
            return;
        }

        String[] mailCodeArr = mailCodeStr.split(",");
        String[] serverCodeArr = serverCodeStr.split(",");

        int[] mailCodes = Arrays.stream(mailCodeArr).mapToInt(Integer::parseInt).toArray();
        int[] serverCodes = Arrays.stream(serverCodeArr).mapToInt(Integer::parseInt).toArray();

        MailDAO dao = new MailDAO();
        boolean result = dao.deleteMails(userID, mailCodes, serverCodes);

        if(result) {
            out.print("{\"status\":\"success\"}");
        } else {
            response.sendError(500, "ER");
        }
    } catch(Exception e) {
        response.sendError(500, "Server error: " + e.getMessage());
    }
%>
