
<%@page import="DAO.TodoDAO"%>
<%@page import="DAO.UserDAO"%>
<%@ page contentType="application/json; charset=UTF-8" %>
<%
try {
    // 1. 파라미터 수신
    String writer = (String) session.getAttribute("uid");
    if (writer == null) {
        writer = request.getParameter("writer");
    } // Postman 디버깅용
    int serverCode = Integer.parseInt(request.getParameter("server_code"));
    String title = request.getParameter("todo_title");
    String content = request.getParameter("todo_content");
    String tags = request.getParameter("tags"); // 콤마 구문자 문자열 (user1,user2)
    String postDate = request.getParameter("post_date"); // 년-월-일 값

    // 2. UID 존재 여부 확인
    UserDAO userDao = new UserDAO();
    String[] tagArray = tags.split(",");
    for (String tag : tagArray) {
        if (!userDao.isIdExist(tag)) {
            out.print("{ \"error\": \"해당 UID 없음: " + tag + "\" }");
            return;
        } else if (!userDao.isUserInServer(tag, serverCode)) {
            out.print("{ \"error\": \"해당 UID는 서버에 속해있지 않음: " + tag + "\" }");
            return;
        }
    }

    // 3. DAO 호출
    TodoDAO dao = new TodoDAO();
    String result = dao.todoInsert(serverCode, title, content, writer, tags, postDate);

    // 4. 결과 응답
    out.print("{ \"result\": \"" + result + "\" }");

} catch (Exception e) {
    response.setStatus(500);
    out.print("{ \"error\": \"할일 생성 실패: " + e.getMessage() + "\" }");
}
%>
