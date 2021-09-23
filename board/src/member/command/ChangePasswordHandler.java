package member.command;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import auth.service.User;
import member.service.ChangePasswordService;
import member.service.DuplicateIdException;
import member.service.InvalidPasswordException;
import member.service.JoinRequest;
import member.service.JoinService;
import member.service.MemberNotFoundException;
import mvc.command.CommandHandler;


// 웹 브라우저의 암호 변경 요청을 처리할 ChangePasswordHandler 클래스를 작성한다.
public class ChangePasswordHandler implements CommandHandler {

	private static final String FORM_VIEW =
			"/WEB-INF/view/changePwdForm.jsp";

	private ChangePasswordService changePwdSvc =
			new ChangePasswordService();
	
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
	// 즉, processForm() 메서드는 데이터가 올바르지 않을 경우,
	// errors 맵 객체에 (키, TRUE)쌍을 추가한다.
	// errors 맵 객체에 데이터가 존재한다는 것은 데이터에 에러가 있다는 것을 의미한다.
	// 보통, 폼에 입력한 값이 잘못되면, 에러 화면을 보여주기보다는
	// 알맞은 에러 메시지와 함께 입력폼을 다시 보여준다.
	private String processForm(HttpServletRequest req, HttpServletResponse res) {
		return FORM_VIEW;
	}
	
	// processSubmit() 메서드는 폼 전송을 처리한다.
	private String processSubmit(HttpServletRequest req,
			HttpServletResponse res) throws Exception {

		User user = (User)req.getSession().getAttribute("authUser");
	
		// 에러 정보를 담을 맵 객체를 생성하고, "errors" 속성에 저장한다.
		Map<String, Boolean> errors = new HashMap<>();
		req.setAttribute("errors", errors);
		
		// 현재 패스워드 curPwd와 새로운 패스워드 newPwd 요청 파라미터 값을 구한다.
		String curPwd = req.getParameter("curPwd");
		String newPwd = req.getParameter("newPwd");
		
		// curPwd 파라미터 값이 없는 경우, errors 맵 객체에 에러 코드를 추가한다.
		if(curPwd == null || curPwd.isEmpty()) {
			errors.put("curPwd", Boolean.TRUE);
		}
		
		// newPwd 파라미터 값이 없는 경우, errors 맵 객체에 에러 코드를 추가한다.
		if(newPwd == null || newPwd.isEmpty()) {
			errors.put("newPwd", Boolean.TRUE);
		}
		
		// 폼에 입력한 데이터가 올바르지 않으면 다시 폼을 보여주게 된다.
		if(!errors.isEmpty()) {
			return FORM_VIEW;
		}
		
		// ChangePasswordService의 changePassword() 메서드를 실행한다.
		// 가입 처리에 성공하면, 성공 결과를 보여줄 뷰 경로를 리턴한다.
		try {
			changePwdSvc.changePassword(user.getId(),
					curPwd, newPwd);
			return "/WEB-INF/view/changePwdSuccess.jsp";
		
		// 현재 암호(curPwd)가 올바르지 않아 익셉션이 발생하면
		// 관련 에러 코드를 추가하고, 폼 뷰 경로를 리턴한다.
		} catch (InvalidPasswordException e) {
			errors.put("badCurPwd", Boolean.TRUE);
			return FORM_VIEW;
		
		// 암호를 변경할 회원 아이디가 존재하지 않아 익셉션이 발생하면
		// 잘못된 요청을 의미하는 400(SC_BAD_REQUEST) 상태 코드를 응답으로 전송한다.
		} catch (MemberNotFoundException e) {
			res.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}
	}
}
