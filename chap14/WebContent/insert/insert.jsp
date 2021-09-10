<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<%
	request.setCharacterEncoding("utf-8");

String memberID = request.getParameter("memberID");
String password = request.getParameter("password");
String name = request.getParameter("name");
String email = request.getParameter("email");

// Class.forName("com.mysql.cj.jdbc.Driver");

Connection conn = null;
PreparedStatement pstmt = null;

try{
	String jdbcDriver
	= "jdbc:mysql://localhost:3306/chap14?"
     + "characterEncoding=UTF-8&serverTimezone=UTC";
	String dbUser = "jspexam";
	String dbPass = "jsppw";

	conn = DriverManager.getConnection(jdbcDriver, dbUser, dbPass);
	pstmt =conn.prepareStatement("insert into MEMBER values(?,?,?,?)");
	pstmt.setString(1, memberID);
	pstmt.setString(2, password);
	pstmt.setString(3, name);
	pstmt.setString(4, email);
	
	pstmt.executeUpdate();
		
	}finally{
			// 6. 사용한 Statement 종료
			// Statement의 사용이 끝나면 close() 메서드를 호출해서
			// 사용한 자원을 시스템에 반환한다.
			if(pstmt != null)
				try{
					pstmt.close();
				} catch(SQLException ex){
				}
			
			// 7. 커넥션 종료
			if(conn != null) try {
				conn.close();
			} catch(SQLException ex){
				}
			}
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>삽입</title>
</head>
<body>

MEMBER 테이블에 새로운 레코드를 삽입했습니다.

</body>
</html>