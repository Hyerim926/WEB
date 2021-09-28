package article.command;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import article.model.Article;
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

		if (req.getMethod().equalsIgnoreCase("GET")) {
			return proccessForm(req, res);
		} else if (req.getMethod().equalsIgnoreCase("POST")) {
			return processSubmit(req, res);
		} else {
			res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return null;
		}
	}

	// GET 방식 요청 시 processForm() 메서드 실행 - 폼뷰 보여줌
	private String proccessForm(HttpServletRequest req, HttpServletResponse res) {
		return FORM_VIEW;
	}

	// POST 방식 요청시 processSubmit() 메서드 실행 - 에러가 있을 시 폼뷰 리턴 / 성공적으로 작성이 될 경우 성공뷰 리턴
	private String processSubmit(HttpServletRequest req, HttpServletResponse res) {
		Map<String, Boolean> errors = new HashMap<>();
		
		WriteRequest writeReq = createWriteRequest(req);
		writeReq.validate(errors);
		
		if(!errors.isEmpty()) {
			return FORM_VIEW;
		}
		
		int newArticle = writeService.write(writeReq);
		
		req.setAttribute("newArticle", newArticle);
		
		return "/WEB-INF/view/newArticleSuccess.jsp";
	}

	private WriteRequest createWriteRequest(HttpServletRequest req) {
		return new WriteRequest(req.getParameter("title"), req.getParameter("content"));
	}

}
