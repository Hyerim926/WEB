package member.command;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import member.service.DuplicateIdException;
import member.service.JoinRequest;
import member.service.JoinService;
import mvc.command.CommandHandler;

// JoinHandler는 다음과 같은 역할을 한다
// GET 방식으로 요청이 오면 폼을 보여주는 뷰인 joinForm.jsp를 리턴한다
// POST 방식으로 요청이 오면 회원가입을 처리하고 결과를 보여주는 뷰를 리턴한다.
// 이때, 입력 데이터가 잘못된 경우에는 다시 joinForm.jsp를 뷰로 리턴한다.
// 아울러, 회원가입에 성공한 경우, joinSuccess.jsp를 뷰로 리턴한다.
// JoinHandler 클래스는 CommandHandler 인터페이스를 구현
public class JoinHandler implements CommandHandler {

	private static final String FORM_VIEW = "/WEB-INF/view/joinForm.jsp";
	// 회원 가입 기능을 제공하는 JoinService 생성
	private JoinService joinService = new JoinService();

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
	// 즉, processForm() 메서드는 데이터가 올바르지 않을 경우, errors 맵 객체에 (키, TRUE)쌍을 추가한다
	// errors 맵 객체에 데이터가 존재한다는 것은 데이터에 에러가 있다는 것 의미
	// 보통, 폼에 입력한 값이 잘못되면, 에러화면을 보여주기보다는 알맞은 에러메시지와 함꼐 입력폼을 다시 보여줌
	private String processForm(HttpServletRequest req, HttpServletResponse res) {
		return FORM_VIEW;
	}

	// processForm() 메서드는 폼 전송을 처리한다
	private String processSubmit(HttpServletRequest req, HttpServletResponse res) {
		// 폼에 입력한 데이터를 이용해서 JoinRequest 객체를 생성한다
		JoinRequest joinReq = new JoinRequest();
		joinReq.setId(req.getParameter("id"));
		joinReq.setName(req.getParameter("name"));
		joinReq.setPassword(req.getParameter("password"));
		joinReq.setConfirmPassword(req.getParameter("confirmPassword"));

		// 에러 정보를 담을 맵 객체를 생성하고, "errors" 속성에 저장한다
		Map<String, Boolean> errors = new HashMap<>();
		req.setAttribute("errors", errors);

		// JoinRequest 객체의 값이 올바른지 검사한다
		// 값이 올바르지 않을 경우, errors 맵 객체에 키를 추가한다
		joinReq.validate(errors);

		// errors 맵 객체에 데이터가 존재하면, 폼 뷰 경로를 리턴한다
		// errors에 데이터가 존재한다는 것은 joinReq 객체의 데이터가
		// 올바르지 않아, 앞서 66행의 joinReq.validate(errors);에서
		// errors에 에러와 관련된 키를 추가했다는 것을 의미
		// joinReq는 폼에 입력한 데이터를 저장하고 있으므로 폼에 입력한 데이터가 올바르지 않으면 다시 폼을 보여주겜됨
		if (!errors.isEmpty()) {
			return FORM_VIEW;
		}

		// JoinService의 join() 메서드를 실행한다
		// 가입 처리에 성공하면 다음의 82행에서 성공결과를 보여줄 뷰 경로를 리턴한다
		try {
			joinService.join(joinReq);
			return "/WEB-INF/view/joinSuccess.jsp";
		// 동일한 아이디로 가입한 회원이 존재하면 앞서 82행에서 DuplicateIdException 예외 발생
		// 이경우, errors에 "duplicateId" 키를 추가하고 폼을 위한 뷰 경로를 리턴함
		} catch (DuplicateIdException e) {
			errors.put("duplicateId", Boolean.TRUE);
			return FORM_VIEW;
		}

	}
}
