package article.command;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mvc.command.CommandHandler;

// 웹 요청을 처리할 WriteHandler 구현
// 게시글 작성 폼을 보여주고 폼 전송을 처리하며 GET 방식 / POST 방식 요청을 별도의 메서드에서 처리함
public class WriteHandler implements CommandHandler {

	private static final String FORM_VIEW = "/WEB-INF/view/newArticleForm.jsp";

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

	private String proccessForm(HttpServletRequest req, HttpServletResponse res) {
		return FORM_VIEW;
	}

	private String processSubmit(HttpServletRequest req, HttpServletResponse res) {
		Map<String, Boolean> errors = new HashMap<>();
		req.setAttribute("errors", errors);
		
		if(!errors.isEmpty()) {
			return FORM_VIEW;
		}
		
		return "/WEB-INF/view/newArticleSuccess.jsp";
	}

}
