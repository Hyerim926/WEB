<%@page import="guestbook.service.WriteMessageService"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%
	request.setCharacterEncoding("utf-8");  
%>

<jsp:useBean id="message" class="guestbook.model.Message">
	<jsp:setProperty name = "message" property="*" />	
</jsp:useBean>

<%
	WriteMessageService writeService
		= WriteMessageService.getInstance();

	writeService.write(message);
%>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>방명록 메시지 남김</title>
</head>
<body>

방명록에 메시지를 남겼습니다.

<br>
<a href="list.jsp">[목록보기]</a>



</body>
</html>