<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.Connection"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<%
	request.setCharacterEncoding("utf-8");

String memberID = request.getParameter("memberID");
String name = request.getParameter("name");

int updateCount = 0;

// Class.forName("com.mysql.cj.jdbc.Driver");

Connection conn = null;
Statement stmt = null;

try{
	String jdbcDriver
	= "jdbc:mysql://localhost:3306/chap14?"
     + "characterEncoding=UTF-8&serverTimezone=UTC";
	String dbUser = "jspexam";
	String dbPass = "jsppw";

// MEMBER 테이블의 NAME 칼럼을 변경하는 UPDATE 쿼리문을 생성한다.
String query = "update MEMBER set NAME = '" + name + "'"
				+ "where MEMBERID = '" + memberID + "'";

		//2. 데이터베이스 커넥션 생성
		conn = DriverManager.getConnection(jdbcDriver, dbUser, dbPass);
		
		// 3. Statement 생성
		stmt = conn.createStatement();
		
		// Statement의 executeUpadate() 메서드를 사용하여 쿼리문을 실행한다.
		// 실행 결과로 변경된 레코드 개수를 updateCount 변수에 저장한다.
		updateCount = stmt.executeUpdate(query);
		
	}finally{
			// 6. 사용한 Statement 종료
			// Statement의 사용이 끝나면 close() 메서드를 호출해서
			// 사용한 자원을 시스템에 반환한다.
			if(stmt != null)
				try{
					stmt.close();
				} catch(SQLException ex){
				}
			
			// 7. 커넥션 종료
			if(conn != null) try {
				conn.close();
				System.out.println("7. 커넥션 종료");
			} catch(SQLException ex){
				}
			}
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>이름 변경</title>
</head>
<body>

	<!--  updateCount가 0보다 크면 변경된 값이 존재한 것으로 간주한다. -->
	<% if(updateCount > 0){ %>
	<%= memberID %>의 이름을 <%= name %>(으)로 변경 하였습니다.
	<% } else{ %>
	<%= memberID %> 아이디가 존재하지 않습니다.
	<% } %>

</body>
</html>