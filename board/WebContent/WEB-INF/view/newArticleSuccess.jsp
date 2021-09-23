<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 등록</title>
</head>
<body>
<!-- 게시글 쓰기에 성공했을 때 사용할 newArticleSuccess.jsp 파일을 제작한다. -->
게시글을 등록했습니다.
<br><br>
${ctxPath = pageContext.request.contextPath; ''} <br><br>

<!-- 게시글 목록 보기 이동 링크 제공 -->
<a href="${ctxPath}/article/list.do">[게시글 목록 보기]</a><br><br>

<!-- 게시글 내용 보기 이동 링크 제공. 이때는 게시글 내용 보기 링크를 생성할 때
         조회할 게시글 번호가 필요하기에, WriteArticleHandler에서 전달한
     newArticleNo 속성을 게시글 번호로 사용한다. -->
<a href="${ctxPath}/article/read.do?no=${newArticleNo}">
[게시글 내용 보기]</a>

</body>
</html>
