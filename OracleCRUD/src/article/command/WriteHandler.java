package article.command;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import article.service.WriteRequest;
import article.service.WriteService;
import mvc.command.CommandHandler;

// 웹 요청을 처리할 WriteHandler 구현
// 게시글 작성 폼을 보여주고 폼 전송을 처리하며 GET 방식 / POST 방식 요청을 별도의 메서드에서 처리함
public class WriteHandler implements CommandHandler {

	private static final String FORM_VIEW = "/WEB-INF/view/newArticleForm.jsp";
	private WriteService writeService = new WriteService();

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {

		// equalsIgnoreCase : 같은 값 확인 시, 대소문자 구분 안함
		// equals : 같은 값 확인 시 대소문자 구분함
		if (req.getMethod().equalsIgnoreCase("GET")) {
			return proccessForm(req, res);
		} else if (req.getMethod().equalsIgnoreCase("POST")) {
			return processSubmit(req, res);
		} else {
			// static int SC_METHOD_NOT_ALLOWED는 지정된 메서드가 식별된 리소스에 대해
			// 허용되지 않음을 나타내는 상태코드(405)이며 setStatus()로 405 응답 코드를 전송함
			res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return null;
		}
	}

	// GET 방식 요청 시 processForm() 메서드 실행 - 폼뷰 보여줌
	private String proccessForm(HttpServletRequest req, HttpServletResponse res) {
		return FORM_VIEW;
	}

	// POST 방식 요청시 processSubmit() 메서드 실행 - 성공적으로 작성이 될 경우 성공뷰 리턴
	private String processSubmit(HttpServletRequest req, HttpServletResponse res) {
		// 에러를 담을 Map errors 객체 생성
		Map<String, Boolean> errors = new HashMap<String, Boolean>();

		// createWriteRequest(req)의 리턴값을 WriteRequest의 객체인 writeReq에 저장함
		// view에서 입력한 값이 비로소 여기서 저장되는 것!
		WriteRequest writeReq = createWriteRequest(req);

		// writeReq 객체가 유효한지 validate() 메서드로 검사하고
		writeReq.validate(errors);

		// 에러가 존재한다면 FORM_VIEW를 다시 보여줌
		if (!errors.isEmpty()) {
			return FORM_VIEW;
		}

		// view에서 가져온 값들이 담긴 writeReq를 파라미터값으로 write() 실행하면
		// WriteService클래스의 toArticle()메서드에 의해 Article 객체에 title과 content가 저장됨
		int newArticleNo = writeService.write(writeReq);

		// 새로운 newArticleNo HttpServletRequest req의 newArticleNo 속성을 저장함
		req.setAttribute("newArticleNo", newArticleNo);

		// 값들이 모두 저장이 되고 나서야 성공폼을 보여줌
		return "/WEB-INF/view/newArticleSuccess.jsp";
	}

	// view와 서버가 소통하는 메서드, WriteRequest객체에 HttpServletRequest로 요청된 req값으로부터
	// getParameter()로 title과 content를 가져오는 것
	private WriteRequest createWriteRequest(HttpServletRequest req) {
		return new WriteRequest(req.getParameter("title"), req.getParameter("content"));
	}

}