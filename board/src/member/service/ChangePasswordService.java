package member.service;

import java.sql.Connection;
import java.sql.SQLException;

import javax.management.RuntimeErrorException;

import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;
import member.dao.MemberDao;
import member.model.Member;


// 암호 변경 기능을 제공할 ChangePasswordService 클래스를 작성한다.
public class ChangePasswordService {

	private MemberDao memberDao = new MemberDao();
	
	// 암호 변경 기능을 구현할 changePassword() 메서드를 선언한다.
	// userId는 암호를 변경할 회원 아이디를 나타내며,
	// curPwd는 현재 암호를, newPwd는 변경할 새로운 암호를 의미한다.
	public void changePassword(String userId, String curPwd,
			String newPwd) {
		
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			
			// userId에 해당하는 Member 데이터를 구한다.
			Member member = memberDao.selectById(conn, userId);
			
			// 암호를 변경할 회원 데이터가 존재하지 않는 경우
			// MemberNotFoundException 예외 처리를 발생시킨다.
			if(member == null) {
				throw new MemberNotFoundException();
			}

			// curPwd가 회원의 실제 암호와 일치하지 않는 경우
			// InvalidPasswordException 예외 처리를 발생시킨다.
			if(!member.matchPassword(curPwd)) {
				throw new InvalidPasswordException();
			}
			
			// member 객체의 암호 데이터를 변경한다.
			member.changePassword(newPwd);
			memberDao.update(conn, member);
			// 변경한 암호 데이터를 DB 테이블에 반영한다.
			conn.commit();
			
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn);
		}
	}
}
