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

	// member 테이블에서 memberid 칼럼 값이 id 파라미터와 동일한 데이터를 조회
	// 데이터가 존재하면 25~28행의 코드를 이용해 Member 객체를 생성
	// JDBC에서 날짜 시간 타입은 Timestamp이고 Member에서 사용한 시간 관련 타입은 Date이기 때문에, Member 객체를 생성할 떄
	// 27행처럼 Timestamp를 Date로 변환함. 데이터가 존재하지 않으면 Member 객체를 생성하지 않으므로 null을 리턴함
	public Member selectById(Connection conn, String id) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement("select * from member where memberid = ?");
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			Member member = null;
			if (rs.next()) {
				member = new Member(rs.getString("memberid"), rs.getString("name"), rs.getString("password"),
						toDate(rs.getTimestamp("regdate")));
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
		try (PreparedStatement pstmt = conn.prepareStatement("insert into member values(?,?,?,?)")) {
			pstmt.setString(1, mem.getId());
			pstmt.setString(2, mem.getName());
			pstmt.setString(3, mem.getPassword());
			pstmt.setTimestamp(4, new Timestamp(mem.getRegDate().getTime()));
			pstmt.executeUpdate();
		}
	}
}
