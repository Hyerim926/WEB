<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인</title>
</head>
<body>
<!-- 로그인 폼을 보여줄 loginForm.jsp 웹 문서를 작성함 -->
<form action="login.do" method="post">
<c:if test="${errors.idOrPwNotMatch}">
아이디와 암호가 일치하지 않습니다.
</c:if>
<p>
	아이디:<br><input type="text" name="id" value="${param.id}">
	<c:if test="${errors.id}">ID를 입력하세요.</c:if>
</p>
<p>
	암호:<br><input type="password" name="password"
	 value="${param.password}">
	<c:if test="${errors.password}">암호를 입력하세요.</c:if>
</p>
<input type="submit" value="로그인">
</form>
</body>
</html>