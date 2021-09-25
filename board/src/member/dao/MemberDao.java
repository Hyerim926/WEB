package member.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import jdbc.JdbcUtil;
import member.model.Member;

public class MemberDao {

	// member 테이블에서 memberid 칼럼 값이 id 파라미터와 동일한 데이터를 조회한다.
	// 데이터가 존재하면 다음 29행~36행의 코드를 이용해서 Member 객체를 생성한다.
	// JDBC에서 날짜 시간 타입은 Timestamp이고 Member에서 사용한 시간 관련 타입은
	// Date이기 때문에, Member 객체를 생성할 때
	// 다음의 36행 처럼 Timestamp를 Date로 변환한다.
	// 데이터가 존재하지 않으면 Member 객체를 생성하지 않으므로 null을 리턴한다.
	public Member selectById(Connection conn, String id) throws SQLException {
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt =
		conn.prepareStatement("select * from member where memberid = ?");
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			Member member = null;
			if(rs.next()) {
				member = new Member(
						rs.getString("memberid"),
						rs.getString("name"),
						rs.getString("password"),
						toDate(rs.getTimestamp("regdate"))				
						);
			}
			return member;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}
	private Date toDate(Timestamp date) {
		return date == null ? null : new Date(date.getTime());
	}
	
	// member 테이블에 데이터를 추가하기 위한 insert() 메서드 정의함
	public void insert(Connection conn, Member mem) throws SQLException {
		try(PreparedStatement pstmt =
				conn.prepareStatement("insert into member values(?,?,?,?)")){
			pstmt.setString(1, mem.getId());
			pstmt.setString(2, mem.getName());
			pstmt.setString(3, mem.getPassword());
			pstmt.setTimestamp(4, new Timestamp(mem.getRegDate().getTime()));
			pstmt.executeUpdate();
		}
	}
	
	// 암호 변경 기능을 구현하기 위해 Member와 MemberDao 두 클래스에
    // 코드를 추가해야 한다. 먼저, Member 클래스에 changePassword() 메서드를 추가했고,
	// 다음으로 MemberDao 클래스에서 member 테이블의 데이터를 수정하기 위한
	// update() 메서드를 추가한다.
	// MemberDao의 update() 메서드는 member 테이블의 memberid 필드가
	// member.getId()와 같은 레코드의 name과 password 필드 값을 변경한다.
	public void update(Connection conn, Member member) throws SQLException {
		try(PreparedStatement pstmt = conn.prepareStatement
	("update member set name = ?, password = ? where memberid = ?"))
		{
			pstmt.setString(1, member.getName());
			pstmt.setString(2, member.getPassword());
			pstmt.setString(3, member.getId());
			pstmt.executeUpdate();
		}
	}
	
	
}
