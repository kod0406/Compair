<%@ page contentType="application/json; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="DAO.MailDAO"%>
<%@ page import="DAO.ServerDAO"%>
<%@ page import="DAO.UserDAO"%>
<%@ page import="user.Mail"%>
<%@ page import="java.io.*"%>
<%@ page import="org.apache.commons.fileupload2.core.*"%>
<%@ page import="org.apache.commons.fileupload2.jakarta.servlet6.*"%>
<%@ page import="util.FileUtil"%>
<%@ page import="util.MailFileUtil"%>
<%@ page import="java.util.*"%>
<%@ page import="java.nio.charset.StandardCharsets"%>
<%@ page
	import="org.apache.commons.fileupload2.core.DiskFileItemFactory"%>
<%@ page
	import="org.apache.commons.fileupload2.jakarta.servlet6.JakartaServletFileUpload"%>

<%
request.setCharacterEncoding("UTF-8");
%>

<%
String userID = null;
if (session.getAttribute("uid") != null) {
	userID = (String) session.getAttribute("uid");
}

// 파일 업로드 처리
String receiver = "";
String subject = "";
String content = "";
String attachment = null;
int serverCode = 0;
byte[] fileData = null;

try {
	// Multipart 요청 파싱
	DiskFileItemFactory factory = DiskFileItemFactory.builder().get();
	JakartaServletFileUpload upload = new JakartaServletFileUpload(factory);
	List<FileItem> items = upload.parseRequest(request);

	// 필드 값 추출
	for (FileItem item : items) {
		if (item.isFormField()) {
	String fieldName = item.getFieldName();
	String value = item.getString(StandardCharsets.UTF_8);
	switch (fieldName) {
	case "receiver":
		receiver = value;
		break;
	case "subject":
		subject = value;
		break;
	case "content":
		content = value;
		break;
	case "server_code":
		serverCode = Integer.parseInt(value);
		break;
	}
		} else {
	if ("attachment".equals(item.getFieldName())) {
		String fullPath = item.getName();
		// 파일명만 추출하는 부분 추가
		String fileName = new File(fullPath).getName();
		attachment = fileName; // 순수 파일명 저장

		fileData = item.get();
		if (attachment != null && !attachment.isEmpty()) {
			String root = application.getRealPath("");
			MailFileUtil.saveMailAttachment(root, attachment, fileData);
		}
	}
		}
	}

	// 필수 필드 검증
	if (subject.isEmpty() || content.isEmpty() || receiver.isEmpty()) {
		out.print("{\"success\": false, \"error\": \"필수 입력 항목이 누락되었습니다.\"}");
		return;
	}

	// 메일 객체 생성
	Mail mail = new Mail();
	mail.setReceiver(receiver);
	mail.setMail_title(subject);
	mail.setTodoContent(content);
	mail.setAttachment(attachment);
	mail.setServer_code(serverCode);
	mail.setWriter(userID);

	UserDAO userdao = new UserDAO();
	ServerDAO sdao = new ServerDAO();
	if (!userdao.isIdExist(mail.getReceiver())) {
	    out.print("{\"success\": false, \"error\": \"서버에 존재하지 않는 유저입니다.\"}");
	} else if (!sdao.isUserInServer(mail.getReceiver(), mail.getServer_code())) {
	    out.print("{\"success\": false, \"error\": \"다른 서버에 존재하는 유저입니다.\"}");
	} else {
	    boolean result = new MailDAO().insertMail(mail);

	    if (result) {
	        out.print("{\"success\": true}");
	    } else {
	        out.print("{\"success\": false, \"error\": \"메일 전송 실패\"}");
	    }
	}

} catch (Exception e) {
	e.printStackTrace();
	out.print("{\"success\": false, \"error\": \"오류 발생: " + e.getMessage() + "\"}");
}
%>
