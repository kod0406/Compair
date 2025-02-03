<%@ page contentType="text/html" pageEncoding="utf-8" %>
<%@ page import="java.util.*" %>
<%@ page import="org.apache.commons.fileupload2.core.*" %>
<%@ page import="org.apache.commons.fileupload2.jakarta.servlet6.*"%>
<%@ page import="org.apache.commons.io.*"%>
<%@ page import="util.FileUtil" %>
<%@ page import="DAO.FeedDAO" %>
<%@ page import="java.nio.charset.StandardCharsets" %>
<%
    request.setCharacterEncoding("UTF-8");

    String jsonstr = null, ufname = null;
    byte[] ufile = null;
    
    DiskFileItemFactory factory = DiskFileItemFactory.builder().get();

    JakartaServletFileUpload sfu = new JakartaServletFileUpload(factory);
    List items = sfu.parseRequest(request);
    System.out.println("Content Type: " + request.getContentType());
    Iterator iter = items.iterator();
    
    while(iter.hasNext()) {
        FileItem item = (FileItem) iter.next();
        String name = item.getFieldName();
        if(item.isFormField()) {
        	String value = item.getString(StandardCharsets.UTF_8);
        	System.out.println("1번째 디버그" + value);
            if (name.equals("jsonstr")) {
            	jsonstr = value;
            	System.out.println("jsonstr은? " + jsonstr);
            }
        }
        else {
            if (name.equals("images")) {
                ufname = item.getName();
                ufile = item.get();
                System.out.println("2번째 디버그  " + ufname);
                System.out.println("3번째 디버그  " + ufile);
				if(ufname != null && !ufname.isEmpty() && ufile != null && ufile.length > 0){
					String root = application.getRealPath(java.io.File.separator);
					FileUtil.saveImage(root, ufname, ufile);
					System.out.println(root + "업로드 완료");
				}
            }
        }

        System.out.println("name " + name);
    }
    FeedDAO dao = new FeedDAO();
    //System.out.println("어디가 문제일까요?");
    if(dao.insert(jsonstr)){
    	out.print("OK");
    	System.out.println("OK");
    } else out.print("ER");
    
%>

