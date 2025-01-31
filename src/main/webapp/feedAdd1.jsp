<%@ page contentType="text/html" pageEncoding="utf-8" %>
<%@ page import="java.util.*" %>
<%@ page import="org.apache.commons.fileupload2.core.*" %>
<%@ page import="org.apache.commons.fileupload2.jakarta.servlet6.*"%>
<%@ page import="org.apache.commons.io.*"%>
<%@ page import="util.FileUtil" %>
<%@ page import="DAO.FeedDAO" %>
<%@ page import="java.nio.charset.StandardCharsets" %>
<%
    request.setCharacterEncoding("utf-8");

    String jsonstr = null, ufname = null;
    byte[] ufile = null;
    
    DiskFileItemFactory factory = DiskFileItemFactory.builder().get();

    JakartaServletFileUpload sfu = new JakartaServletFileUpload(factory);
    List items = sfu.parseRequest(request);
    
    Iterator iter = items.iterator();
    while(iter.hasNext()) {
        FileItem item = (FileItem) iter.next();
        String name = item.getFieldName();
        if(item.isFormField()) {
        	String value = item.getString(StandardCharsets.UTF_8);
        	System.out.println("1번째 디버그" + value);
            if (name.equals("jsonstr")) jsonstr = value;
        }
        else {
            if (name.equals("image")) {
                ufname = item.getName();
                ufile = item.get();
                System.out.println("2번째 디버그" + ufname);
                System.out.println("3번째 디버그" + ufile);
                String root = application.getRealPath(java.io.File.separator);
                System.out.println(root);
                FileUtil.saveImage(root, ufname, ufile);
                out.print("이미지 업로드가 완료되었습니다.");
            }
        }

        System.out.println(name);
    }
    FeedDAO dao = new FeedDAO();
    System.out.println(jsonstr);
%>

