<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
	String idValue = request.getParameter("id");

	Connection conn = null;
	PreparedStatement pstmtItem = null;
	PreparedStatement pstmtDetail = null;

	String jdbcDriver = "jdbc:mysql://localhost:3306/chap14?" + "characterEncoding=UTF-8&serverTimezone=UTC";
	String dbUser = "jspexam";
	String dbPass = "jsppw";

	// 예외 발생 시 처리를 위해 Throwable 객체 참조 occuredException을 null값으로 초기화
	Throwable occuredException = null;

	try {

		int id = Integer.parseInt(idValue);

		conn = DriverManager.getConnection(jdbcDriver, dbUser, dbPass);

		//트랜잭션 처리의 commit, rollback 활용을 위해
		// 오토커밋(자동커밋) 처리로 false로 설정함(핵심)
		conn.setAutoCommit(false);
		// ITEM 테이블에 데이터를 삽입하는 첫번째 쿼리문 실행
		pstmtItem = conn.prepareStatement("insert into ITEM values(?,?)");
		pstmtItem.setInt(1, id);
		pstmtItem.setString(2, "상품이름 " + id);
		pstmtItem.executeUpdate();

		// error 파라미터가 존재할 경우 익셉션(예외처리)을 발생시킴
		if (request.getParameter("error") != null) {
			throw new Exception("의도적 익셉션 발생");
		}

		pstmtDetail = conn.prepareStatement("insert into ITEM_DETAIL values(?,?)");
		pstmtDetail.setInt(1, id);
		pstmtDetail.setString(2, "상세 설명 " + id);
		pstmtDetail.executeUpdate();

		// 트랜잭션 처리과정이 정상처리 완료되면 세션 연결 상태에서 커밋처리
		conn.commit();
		
	} catch (Throwable e) {
		if (conn != null) {
			try {
				// 트랜잭션 처리 과정에서 정상 처리 완료가 아니라면
				// 즉, 예외상황이 발생하면ㄴ rollback 처리
				conn.rollback();
			} catch (SQLException ex) {
			} // 앞서 롤백 처리한 익셉션을 occuredException에  할당함
		}

		occuredException = e;

	} finally {
		// 6. 사용한 Statement 종료
		// Statement의 사용이 끝나면 close() 메서드를 호출해서
		// 사용한 자원을 시스템에 반환한다.
		if (pstmtItem != null)
			try {
				pstmtItem.close();
			} catch (SQLException ex) {
			}

		// 7. 커넥션 종료
		if (conn != null)
			try {
				conn.close();
			} catch (SQLException ex) {
			}
	}
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ITEM 값 입력</title>
</head>
<body>
	
	<!-- occurexcperion이 null이 아닌경우
			익셉션 예외 처리 메시지 출력 -->
	<%
		if (occuredException != null) {
	%>
	에러가 발생하였음:
	<%=occuredException.getMessage()%>
	<%
		} else {
	%>
	데이터가 성공적으로 들어감
	<%
		}
	%>

</body>
</html>