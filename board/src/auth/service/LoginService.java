package auth.service;

import java.sql.Connection;
import java.sql.SQLException;

import jdbc.connection.ConnectionProvider;
import member.dao.MemberDao;
import member.model.Member;

//LoginService 클래스는 사용자가 입력한 아이디와 암호가 올바른지 검사
//아이디와 암호가 일치하면 로그인한 사용자 정보를 담은 User 객체를 리턴함
public class LoginService {

	// LoginService는 MemberDao를 이용해서 아이디에 해당하는 회원데이터가 존재하는지 확인
	private MemberDao memberDao = new MemberDao();

	// login() 메서드는 아이디와 암호를 파라미터로 전달받도록 구현함
	public User login(String id, String password) {
		try (Connection conn = ConnectionProvider.getConnection()) {
			// 회원 데이터가 존재하지 않거나 암호가 일치하지 않으면 LoginFailException 발생
			Member member = memberDao.selectById(conn, id);
			if (member == null) {
				throw new LoginFailException();
			}
			if (!member.matchPassword(password)) {
				throw new LoginFailException();
			}
			// 암호가 일치하면 회원 아이디와 이름을 담은 User 객체를 생성해서 리턴
			return new User(member.getId(), member.getName());
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
