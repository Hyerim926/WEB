<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 삭제</title>
</head>
<body>
<form action="delete.do" method="post">
<p>
제목<br><br><input type="text" name="title" value="${article.title}">
</p>
<p><br>
내용<br><br>
<textarea rows="5" cols="30" name="content">${article.content}</textarea>
</p>
<input type="submit" value="게시글 삭제">
<input type="button" value="취소"><a href="/OracleCRUD/index.jsp"></a>

</form>
</body>
</html>