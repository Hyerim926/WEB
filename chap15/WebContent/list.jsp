<%@page import="guestbook.service.MessageListView"%>
<%@page import="guestbook.service.GetMessageListService"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
 
 <% // page 파라미터의 값을 이용해서 읽어올 페이지 번호를 생성합니다.
 	String pageNumberStr = request.getParameter("page");
 	int pageNumber = 1;
 	if(pageNumberStr != null){
 		pageNumber = Integer.parseInt(pageNumberStr);
 	}
 	
 	GetMessageListService messageListService =
 			GetMessageListService.getInstance();
 	// GetMessageListService.getMessageList 메서드를 이용해서
 	// 지정한 페이지에 해당하는 메시지 목록을 구합니다.
 	MessageListView viewData =
 			messageListService.getMessageList(pageNumber);
 %>

<!-- viewData를 EL에서 사용할 수 있도록 var 변수로 지정합니다. -->
<c:set var="viewData" value="<%= viewData %>" />
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>방명록 메시지 목록</title>
</head>
<body>
<!-- 신규 메시지 입력 폼을 화면 출력합니다. -->
<form action="writeMessage.jsp" method="post">
이름 : <input type="text" name="guestName"> <br>
암호 : <input type="password" name="password"> <br>
메시지 : <textarea rows="3" cols="30" name="message"></textarea><br>
<input type="submit" value="메시지 남기기" />
</form>
<hr>
<!-- 메시지가 isEmpty 비어서 없는 경우 안내 문구를 화면 출력 합니다. -->
<c:if test="${viewData.isEmpty()}">
등록된 메시지가 없습니다!!
</c:if>
<hr>
<!-- 메시지가 존재할 경우 JSP Core forEach 태그를 이용해서 메시지를
         차례대로 출력합니다. -->
<c:if test="${!viewData.isEmpty()}">
<table border="1">
	<c:forEach var="message" items="${viewData.messageList}">
	
	<tr>
		<td>
		메시지 번호 : ${message.id} <br>
		손님 이름 : ${message.guestName} <br>
		메시지 : ${message.message} <br>
		<a href="confirmDeletion.jsp?messageId=${message.id}">
			[삭제하기]</a>
		</td>
	</tr>	
	</c:forEach>
</table>

<!-- 전체 페이지 목록을 출력합니다. -->
<c:forEach var="pageNum" begin="1" end= "${viewData.pageTotalCount}">
<a href="list.jsp?page=${pageNum}">[${pageNum}]</a>

</c:forEach>
</c:if>

</body>
</html>