<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.Connection"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 목록</title>
</head>
<body>

	MEMBER 테이블의 내용
	<table width="100%" border="1">
		<tr>
			<td>이름</td>
			<td>아이디</td>
			<td>이메일</td>
		</tr>

		<% // 1. JDBC 드라이버 로딩
	   // MySQL 8 이하 버전은 com.mysql.jdbc.Driver 사용함
	   // MySQL 8 이상 버전은 com.mysql.cj.jdbc.Driver 사용함 
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		System.out.println("1. JDBC 드라이버 로딩");
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
	
		try{
			String jdbcDriver
			= "jdbc:mysql://localhost:3306/chap14?"
             + "characterEncoding=UTF-8&serverTimezone=UTC";
			String dbUser = "jspexam";
			String dbPass = "jsppw";
			
		String query = "select * from MEMBER order by MEMBERID";
		
		// 2. 데이터베이스 커넥션 생성
		conn = DriverManager.getConnection(jdbcDriver, dbUser, dbPass);
		
		System.out.println("2. 데이터베이스 커넥션 생성");
		
		// 3. Statement 생성
		stmt = conn.createStatement();
		System.out.println("3. Statement 생성");
		
		// 4. 쿼리 실행
		rs = stmt.executeQuery(query);
		System.out.println("4. 쿼리 실행");
		
		// 5. 쿼리 실행 결과 출력
		while(rs.next()){
		%>
		
		<tr>
			<td><%= rs.getString("NAME") %>
			<td><%= rs.getString("MEMBERID") %>
			<td><%= rs.getString("EMAIL") %>
		</tr>
		<%
			}
		
		}  catch(SQLException ex) {
			out.println(ex.getMessage());
			ex.printStackTrace();
		} finally{
			
			// 6. 사용한 Statement 종료
			if(rs != null)
				try{
					rs.close();
					System.out.println("6. 사용한 Statement 종료 : rs");
				} catch(SQLException ex){
				}
			if(stmt != null)
				try{
					stmt.close();
					System.out.println("6. 사용한 Statement 종료 : stmt");
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
	</table>

</body>
</html>