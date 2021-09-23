package member.command;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import member.service.DuplicateIdException;
import member.service.JoinRequest;
import member.service.JoinService;
import mvc.command.CommandHandler;


// JoinHandler는 다음과 같은 역할을 한다.
// GET 방식으로 요청이 오면 폼을 보여주는 뷰인 joinForm.jsp를 리턴한다.
// POST 방식으로 요청이 오면 회원 가입을 처리하고 결과를 보여주는 뷰를 리턴한다.
// 이때, 입력 데이터가 잘못된 경우에는 다시 joinForm.jsp를 뷰로 리턴한다.
// 아울러, 회원 가입에 성공한 경우 joinSuccess.jsp를 뷰로 리턴한다.
// JoinHandler 클래스는 CommandHandler 인터페이스를 구현한다.
public class JoinHandler implements CommandHandler {

	private static final String FORM_VIEW =
			"/WEB-INF/view/joinForm.jsp";

	// 회원 가입 기능을 제공하는 JoinService 생성
	private JoinService joinService = new JoinService();
	
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
			HttpServletResponse res) {
		// 폼에 입력한 데이터를 이용해서 JoinRequest 객체를 생성한다.
		JoinRequest joinReq = new JoinRequest();
		joinReq.setId(req.getParameter("id"));
		joinReq.setName(req.getParameter("name"));
		joinReq.setPassword(req.getParameter("password"));
		joinReq.setConfirmPassword(req.getParameter
				("confirmPassword"));
		
		// 에러 정보를 담을 맵 객체를 생성하고, "errors" 속성에 저장한다.
		Map<String, Boolean> errors = new HashMap<>();
		req.setAttribute("errors", errors);
		
		// JoinRequest 객체의 값이 올바른지 검사한다.
		// 값이 올바르지 않을 경우, errors 맵 객체에 키를 추가한다.		
		joinReq.validate(errors);
		
		// errors 맵 객체에 데이터가 존재하면, 폼 뷰 경로를 리턴한다.
		// errors에 데이터가 존재한다는 것은 joinReq 객체의 데이터가
		// 올바르지 않아, 앞서 70행의 joinReq.validate(errors);에서
		// errors에 에러와 관련된 키를 추가했다는 것을 의미한다.
		// joinReq는 폼에 입력한 데이터를 저잦ㅇ하고 있으므로,
		// 폼에 입력한 데이터가 올바르지 않으면 다시 폼을 보여주게 된다.
		if(!errors.isEmpty()) {
			return FORM_VIEW;
		}
		
		// JoinService의 join() 메서드를 실행한다.
		// 가입 처리에 성공하면 다음의 87행에서
		// 성공 결과를 보여줄 뷰 경로를 리턴한다.
		try {
			joinService.join(joinReq);
			return "/WEB-INF/view/joinSuccess.jsp";
			
		// 동일한 아이디로 가입한 회원이 존재하면 앞서 87행에서
		// DuplicateIdException 익셉션이 발생한다.
		// 이 경우, errors에 "duplicateId" 키를 추가하고,
		// 폼을 위한 뷰 경로를 리턴한다.
		} catch (DuplicateIdException e) {
			errors.put("duplicateId", Boolean.TRUE);
			return FORM_VIEW;
		}
	}
}
