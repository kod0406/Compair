<%@page import="DAO.ServerDAO"%>
<%@page import="DAO.TodoDAO"%>
<%@page import="DAO.UserDAO"%>
<%@ page contentType="application/json; charset=UTF-8" %>
<%
try {
    String writer = (String) session.getAttribute("uid");
    if (writer == null) {
        writer = request.getParameter("writer");
    }
    int serverCode = Integer.parseInt(request.getParameter("server_code"));
    String title = request.getParameter("todo_title");
    String tags = request.getParameter("tags");
    String postDate = request.getParameter("post_date");
    System.out.println("tags: " + tags);
    UserDAO userDao = new UserDAO();
    ServerDAO serverDao = new ServerDAO();
    String[] tagArray = tags.split(",");
    for (String tag : tagArray) {
        if (!userDao.isIdExist(tag)) {
            out.print("{ \"error\": \"해당 UID 없음: " + tag + "\" }");
            return;
        } else if (!serverDao.isUserInServer(tag, serverCode)) {
            out.print("{ \"error\": \"해당 UID는 서버에 속해있지 않음: " + tag + "\" }");
            return;
        }
    }

    TodoDAO dao = new TodoDAO();
    String result = dao.todoInsert(serverCode, title, writer, tags, postDate);

    out.print("{ \"result\": \"" + result + "\" }");

} catch (Exception e) {
    response.setStatus(500);
    out.print("{ \"error\": \"할일 생성 실패: " + e.getMessage() + "\" }");
}
%>
