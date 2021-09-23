package auth.command;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.jasper.TrimSpacesOption;

import auth.service.LoginFailException;
import auth.service.LoginService;
import auth.service.User;
import member.service.DuplicateIdException;
import member.service.JoinRequest;
import member.service.JoinService;
import mvc.command.CommandHandler;


// LoginHandler 클래스는 앞서 JoinHandler 클래스와 매우 유사하다.
// GET 방식 요청이 오면 폼을 위한 뷰를 리턴하고,
// POST 방식 요청이 오면 LoginService를 이용해서 로그인을 처리한다.
public class LoginHandler implements CommandHandler {

	private static final String FORM_VIEW =
			"/WEB-INF/view/loginForm.jsp";

	private LoginService loginService = new LoginService();
	
	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		// 요청 방식이 GET(또는 get)이면 processForm()을 실행하고,
		// 요청 방식이 POST(또는 post)이면 processSubmit()을 실행한다.
		// 참고로, equls() 메서드는 문자열 비교 시 대소문자를 구분해서,
		// 문자열이 같은 경우 true 리턴하고, 다른 경우 false 리턴함.
		// 하지만, equalsIgnoreCase() 메서드는 문자열 비교 시 대소문자를
		// 구분하지 않음. 즉, 대소문자 구분 없이 문자열만 같다면 true 리턴하고,
		// 다른 경우 false 리턴함.
		// GET 또는 POST가 아니면 405 응답 코드를 전송한다.
		// 참고로, static int	SC_METHOD_NOT_ALLOWED
        // 405 응답 코드 전송 (허용되지 않는 메소드 응답)
        // 지정된 메서드가 식별 된 리소스에 대해 허용되지 않음을 나타내는
		// 상태 코드 (405)를 의미함.
		if(req.getMethod().equalsIgnoreCase("GET")) {
			return processForm(req,res);
		} else if (req.getMethod().equalsIgnoreCase("POST")) {
			return processSubmit(req, res);
		} else {
			res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return null;
		}
	}

	// processForm() 메서드는 폼을 보여주는 뷰 경로를 리턴한다.
	private String processForm(HttpServletRequest req, HttpServletResponse res) {
		return FORM_VIEW;
	}
	
	// processSubmit() 메서드는 폼 전송을 처리한다.
	private String processSubmit(HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		
		// 폼에서 전송한 id 파라미터와 password 파라미터 값을 구한다.
		String id = trim(req.getParameter("id"));
		String password = trim(req.getParameter("password"));
		
		// 에러 정보를 담을 맵 객체를 생성하고, "errors" 속성에 저장한다.
		Map<String, Boolean> errors = new HashMap<>();
		req.setAttribute("errors", errors);

		// id나 password가 없을 경우 에러를 추가한다.
		if(id == null || id.isEmpty()) {
			errors.put("id", Boolean.TRUE);
		}
		if(password == null || password.isEmpty()) {
			errors.put("password", Boolean.TRUE);
		}
		
		// errors 맵 객체에 데이터가 존재하면, 폼 뷰 경로를 리턴한다.
		if(!errors.isEmpty()) {
			return FORM_VIEW;
		}

		try {
			// loginService.login() 메서드를 이용해서 인증을 수행한다.
			// 로그인에 성공하면 User 객체를 리턴한다.
			User user = loginService.login(id, password);
			// User 객체를 세션의 authUser 속성에 저장한다.
			req.getSession().setAttribute("authUser", user);
			// /index.jsp로 리다이렉트한다.
			res.sendRedirect(req.getContextPath()+"/index.jsp");			
			return null;
			// 로그인에 실패해서 LoginFailException이 발생하면 해당 에러를
			// 추가하고, 폼을 위한 뷰를 리턴한다.
		} catch (LoginFailException e) {
			errors.put("idOrPwNotMatch", Boolean.TRUE);
			return FORM_VIEW;
		}
	}
		
		

	private String trim(String str) {
		return str == null ? null : str.trim();
	}
}
