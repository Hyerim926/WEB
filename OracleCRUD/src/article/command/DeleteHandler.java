package article.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import article.model.Article;
import article.service.DeleteService;
import article.service.ReadRecentArticleService;
import mvc.command.CommandHandler;

public class DeleteHandler implements CommandHandler {

	private static final String FORM_VIEW = "/WEB-INF/view/deleteForm.jsp";

	private ReadRecentArticleService recentSerivce = new ReadRecentArticleService();
	private DeleteService deleteService = new DeleteService();

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		if(req.getMethod().equalsIgnoreCase("GET")) {
			return processForm(req, res);
		} if(req.getMethod().equalsIgnoreCase("POST")) {
			return processSubmit(req, res);
		} else {
			res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return null;
		}
	}

	private String processSubmit(HttpServletRequest req, HttpServletResponse res) {
		// 최근 게시글 불러와서
		Article article = recentSerivce.recent();
		// req객체에 article 속성값 설정해줌
		req.setAttribute("article", article);
		
		// 삭제 서비스 처리
		deleteService.delete();
		// 성공폼 리턴
		return "/WEB-INF/view/deleteSuccess.jsp";
	}

	private String processForm(HttpServletRequest req, HttpServletResponse res) {
		Article article = recentSerivce.recent();
		if(article == null) {
			return "/WEB-INF/view/noArticle.jsp";
		}
		req.setAttribute("article", article);
		return FORM_VIEW;
	}

}
