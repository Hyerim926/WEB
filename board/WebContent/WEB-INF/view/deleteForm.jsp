<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 삭제</title>
</head>
<body>
<form action="delete.do" method="post">
<input type="hidden" name="no" value="${delReq.articleNumber}">
선택하신 게시글을 삭제하시겠습니까? <br>
<input type="submit" value="글 삭제 진행">
<a href="list.do">[목록으로 돌아가기]</a>
</form>
</body>
</html>