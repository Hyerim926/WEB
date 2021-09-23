package article.command;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import article.model.Writer;
import article.service.WriteArticleService;
import article.service.WriteRequest;
import auth.service.User;
import mvc.command.CommandHandler;

// 웹 요청을 처리할 WriteArticleHandler를 구현하려고 한다.
// 게시글 작성 폼을 보여주고 폼 전송을 처리하므로,
// 앞서 구현했던 핸들러 클래스들 처럼,
// GET 방식 요청과 POST 방식 요청을 별도의 메서드에서 처리한다.
public class WriteArticleHandler implements CommandHandler {

	private static final String FORM_VIEW
		= "/WEB-INF/view/newArticleForm.jsp";
	private WriteArticleService writeService
		= new WriteArticleService();
	
	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		// equalsIgnoreCase : 같은 값 확인할 때, 대소문자 구분을 하지 않는다.
		// equals : 같은 값 확인할 때, 대소문자 구분을 한다.
		if(req.getMethod().equalsIgnoreCase("GET")) {
			return processForm(req, res);
		} else if (req.getMethod().equalsIgnoreCase("POST")) {
			return processSubmit(req, res);
		} else {
			// static int	SC_METHOD_NOT_ALLOWED
	        // 405 응답 코드 전송 (허용되지 않는 메소드 응답)
	        // 지정된 메서드가 식별 된 리소스에 대해 허용되지 않음을 나타내는
			// 상태 코드 (405) 입니다.
			res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return null;
		}
	}


	private String processForm(HttpServletRequest req, HttpServletResponse res) {
		return FORM_VIEW;
	}
	
	private String processSubmit(HttpServletRequest req, HttpServletResponse res) {

		Map<String, Boolean> errors =
					new HashMap<String, Boolean>();
		
		// 세션에서 로그인한 사용자 정보를 구한다.
		User user = 
			(User)req.getSession(false).getAttribute("authUser");
		
	// user와 HttpServletRequest를 이용해서 WriteRequest 객체를 생성한다.
		WriteRequest writeReq =createWriteRequest(user, req);
		
		// writeReq 객체가 유효한지 validate() 메서드로 검사한다.
		writeReq.validate(errors);
		
		// 에러가 존재하면 FORM_VIEW 폼을 다시 보여준다.
		if(!errors.isEmpty()) {
			return FORM_VIEW;
		}
		
		// WriteArticleService를 이용해서 게시글을 등록하고,
		// 등록된 게시글의 /WEB-INF/view/newArticleSuccess.jsp
		// 처리 값을 리턴 받는다.
		int newArticleNo = writeService.write(writeReq);
		
		// 새로운 newArticleNo request의 newArticleNo 속성을 저장한다.
		// 처리 결과를 보여줄 JSP는 이 속성값을 사용해서 링크를 생성한다.
		req.setAttribute("newArticleNo", newArticleNo);
		
		return "/WEB-INF/view/newArticleSuccess.jsp";
	}


	private WriteRequest createWriteRequest(User user, HttpServletRequest req) {
		return new WriteRequest(new Writer(user.getId(), user.getName()), req.getParameter("title"), req.getParameter("content"));
	}
	
}
