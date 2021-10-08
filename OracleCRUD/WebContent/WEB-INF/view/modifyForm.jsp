<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>최근 게시글 수정</title>
</head>
<body>
<form action="modify.do" method="post">
<p>
제목<br><br><input type="text" name="title" value="${modReq.title}">
<c:if test="${errors.title }">제목을 입력하세요</c:if>
</p>
<p><br>
내용<br><br>
<textarea rows="5" cols="30" name="content">${modReq.content}</textarea>
</p>
<input type="submit" value="게시글 수정">
<a href="/OracleCRUD/index.jsp"><input type="button" value="취소"></a>


</form>
</body>
</html>