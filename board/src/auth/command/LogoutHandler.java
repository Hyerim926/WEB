package auth.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import mvc.command.CommandHandler;

// 로그아웃 기능을 제공하는 LogoutHandler 클래스 제작
// LogoutHandler는 세션이 존재하면 세션을 종료한다.
// 세션을 종료하면 세션에 저장된 "authUser" 속성도 함께 삭제되므로
// 로그아웃 처리된다.
public class LogoutHandler implements CommandHandler {

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		HttpSession session = req.getSession(false);
		if(session != null) {
			session.invalidate();
		}
		
		res.sendRedirect(req.getContextPath()+ "/index.jsp");
		return null;
	}
}
