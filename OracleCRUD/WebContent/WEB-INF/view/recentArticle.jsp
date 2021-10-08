<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>최근 게시글 조회</title>
</head>
<body>
	[조회한 게시글 입니다]
	<p>
		<br>제목<br>
		<br>
		<input type="text" value="${article.title } ">
	</p>
	<p>
		<br>내용<br>
		<br>
		<textarea rows="5" cols="30">${article.content}</textarea>
	</p>
		<a href="/OracleCRUD/index.jsp"><input type="button" value="취소"></a>
</body>
</html>