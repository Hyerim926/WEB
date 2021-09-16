package member.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;
import member.dao.MemberDao;
import member.model.Member;

// 회원 가입 기능을 제공하는 JoinService 클래스 작성
public class JoinService {
	
	private MemberDao memberDao = new MemberDao();
	
	public void join(JoinRequest joinReq) {
		Connection conn = null;
		try {
			// DB 커넥션을 구함
			conn = ConnectionProvider.getConnection();
			// 트랜잭션 적용을 위해 오토커밋을 false값으로 설정
			conn.setAutoCommit(false);
			
			// MemberDao의 selctById()메서드를 이요해서
			// joinReq.getId() 메서드에 해당하는 회원 데이터 구함
			Member member = memberDao.selectById(conn, joinReq.getId());
			
			// 가입하려는 ID에 해당하는 데이터가 이미 존재하면 트랜잭션을 롤백하고
			// DuplicateIdException 중복값 예외처리 발생함
			if(member != null) {
				JdbcUtil.rollback(conn);
				throw new DuplicateIdException();
			}

			// joinReq를 이용해서 Member 객체를 생성하고, MemberDao의 insert() 메서드를 실행해서
			// 회원데이터를 테이블에 삽입함
			memberDao.insert(conn, new Member(
					joinReq.getId(),
					joinReq.getName(),
					joinReq.getPassword(),
					new Date())
					);
			conn.commit();
			
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn);
		}
		
	}
}
