<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>오라클을 활용한 게시판입니다</title>
</head>
<body>
${ctxPath = pageContext.request.contextPath ; ''}
<a href="${ctxPath}/write.do">[게시글 등록]</a><br><br>
<a href="${ctxPath}/recent.do">[최근 게시글 보기]</a><br><br>
<a href="${ctxPath}/modify.do">[최근 게시글 수정]</a><br><br>
<a href="${ctxPath}/delete.do">[최근 게시글 삭제]</a>

</body>
</html>