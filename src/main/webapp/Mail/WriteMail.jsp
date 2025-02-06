<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<script>
    function check(){
        if(frm.subject.value == ""){
            alert('제목은 필수 입력 사항입니다.');
            frm.subject.focus();
            return false;
        }
    }
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>메일 작성</title>
</head>
<body>
<form action="WriteAction.jsp" method="post" name="frm" enctype="multipart/form-data" onsubmit="return check()">
<input type="hidden" name="sender" value="">
<% 
    // URL 파라미터에서 receiver 값 추출
    String receiver = request.getParameter("receiver") != null 
                        ? request.getParameter("receiver") 
                        : "";
%>
받는 사람 : <input type="text" name="receiver" value="<%= receiver %>">
제목 : <input type="text" name="subject"><br>
내용 : <textarea rows="10" cols="20" name="content"></textarea>
첨부 파일:<input type="file" name="attachment"><br>
<input type="submit" value="보내기"><input type="reset" value="취소" onclick="window.close()">
</form>
</body>
</html>
