<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>최근 게시글 보기</title>
</head>
<body>
<form action="recent.do" method="post">
<p>
[최근 게시글 보기]<br>
제목: ${articleData.article.title}<br>
글 내용: <br> ${articleData.article.content}<br><br>
<a href="index.jsp"><input type="button" value="처음으로"></a>
</form>


</body>
</html>