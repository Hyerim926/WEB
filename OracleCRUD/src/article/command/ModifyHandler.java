package article.command;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import article.model.Article;
import article.service.ModifyRequest;
import article.service.ModifyService;
import article.service.ReadRecentArticleService;
import mvc.command.CommandHandler;

public class ModifyHandler implements CommandHandler{
	
	private static final String FORM_VIEW = "/WEB-INF/view/modifyForm.jsp";
	
	private ReadRecentArticleService recentService = new ReadRecentArticleService();
	private ModifyService modifyService = new ModifyService();

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		if(req.getMethod().equalsIgnoreCase("GET")) {
			return processForm(req, res);
		} else if(req.getMethod().equalsIgnoreCase("POST")) {
			return processSubmit(req, res);
		} else {
			res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return null;
			
		}
	}

	// 최신글 조회해서 값을 수정폼에 보여줌
	private String processForm(HttpServletRequest req, HttpServletResponse res) {
		Article article = recentService.recent();
		if(article == null) {
			return "/WEB-INF/view/noArticle.jsp";
		}
		ModifyRequest modReq = new ModifyRequest(article.getTitle(), article.getContent());
		
		req.setAttribute("modReq", modReq);
		return FORM_VIEW;
	}

	// 바뀐 값으로 수정해주고, 만약 에러 있을 경우 원래 폼화면으로 되돌림
	private String processSubmit(HttpServletRequest req, HttpServletResponse res) {
		ModifyRequest modReq = new ModifyRequest(req.getParameter("title"),req.getParameter("content"));
		req.setAttribute("modReq", modReq);
		
		Map<String, Boolean> errors = new HashMap<>();
		req.setAttribute("errors", errors);
		modReq.validate(errors);
		if(!errors.isEmpty()) {
			return FORM_VIEW;
		}
		modifyService.modify(modReq);
		return "/WEB-INF/view/modifySuccess.jsp";
	}

}
