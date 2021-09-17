package member.service;

import java.sql.Connection;
import java.sql.SQLException;

import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;
import member.dao.MemberDao;
import member.model.Member;

public class ChangePasswordService {

	private MemberDao memberDao = new MemberDao();

	public void changePassword(String userId, String curPwd, String newPwd) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);

			// userId에 해당하는 Member 데이터를 구함
			Member member = memberDao.selectById(conn, userId);
			// selectById()메서드로 가져온 member 값이 null일 떄 MNF 예외 처리
			// 회원 데이터 존재 않는 경우
			if (member == null) {
				throw new MemberNotFoundException();
			}
			// matchPassword() 메서드로 현재 패스워드와 맞지 않을 떄 IP 예외 처리
			// curPwd가 회원의 실제 암호 일치 않는 경우
			if (!member.matchPassword(curPwd)) {
				throw new InvalidPasswordException();
			}
			// 두 가지 경우에 해당하지 않을 때 changePassword()메서드로 
			// 암호를 update()메서드를 이용해 newPwd로 변경해줌
			member.changePassword(newPwd);
			memberDao.update(conn, member);
			conn.commit();
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

}
