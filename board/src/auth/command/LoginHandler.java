package auth.command;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import auth.service.LoginFailException;
import auth.service.LoginService;
import auth.service.User;
import mvc.command.CommandHandler;

// LoginHandler 클래스는 앞서 JoinHandler 클래스와 매우 유사
// GET방식 요청이 오면 폼을 위한 뷰를 리턴하고, POST 방식 요청이 오면 LoginService를 이용해서 로그인 처리
public class LoginHandler implements CommandHandler {

	private static final String FORM_VIEW = "/WEB-INF/view/loginForm.jsp";
	private LoginService loginService = new LoginService();

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {

		// 요청 방식이 GET이면 processForm()을 실행하고
		// 요청 방식이 POSR이면 processSubmit()을 실행함
		// GET 또는 POST가 아니면 405 응답코드 전송
		// 참고로, static int SC_METHOD_NOT_ALLOWED 405 응답 코드 전송
		// 허용되지 않는 메소드 응답(지정된 메서드가 식별된 리소스에 대해 허용되지 않음을 나타내는 상태코드(405)
		if (req.getMethod().equalsIgnoreCase("GET")) {
			return processForm(req, res);
		} else if (req.getMethod().equalsIgnoreCase("POST")) {
			return processSubmit(req, res);
		} else {
			res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return null;
		}
	}

	// processForm() 메서드는 폼을 보여주는 뷰 경로를 리턴한다
	private String processForm(HttpServletRequest req, HttpServletResponse res) {
		return FORM_VIEW;
	}

	// processForm() 메서드는 폼 전송을 처리한다
	private String processSubmit(HttpServletRequest req, HttpServletResponse res) throws Exception {

		String id = trim(req.getParameter("id"));
		String password = trim(req.getParameter("password"));

		// 에러 정보를 담을 맵 객체를 생성하고, "errors" 속성에 저장
		Map<String, Boolean> errors = new HashMap<>();
		req.setAttribute("errors", errors);

		// id나 password가 없을 경우 에러 추가
		if (id == null || id.isEmpty()) {
			errors.put("id", Boolean.TRUE);
		}
		if (password == null || password.isEmpty()) {
			errors.put("password", Boolean.TRUE);
		}
		// errors 맵 객체에 데이터가 존재하면, 폼 뷰 경로 리턴
		if (!errors.isEmpty()) {
			return FORM_VIEW;
		}

		try {
			// loginService.login() 메서드를 이용해서 인증 수행
			// 로그인에 성공하면 User 객체 리턴
			User user = loginService.login(id, password);
			// User 객체를 세션의 authUser 속성에 저장
			req.getSession().setAttribute("authUser", user);
			// .index.jsp로 리다이렉트함
			res.sendRedirect(req.getContextPath() + "/index.jsp");
			return null;
			// 로그인에 실패해서 LoLoginFailException이 발생하면 해당 에러를 추가하고 로그인 폼 뷰를 리턴
		} catch (LoginFailException e) {
			errors.put("idOrPwNotMatch", Boolean.TRUE);
			return FORM_VIEW;
		}

	}

	private String trim(String str) {
		return str == null ? null : str.trim();
	}

}
