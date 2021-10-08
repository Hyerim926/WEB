package article.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import article.model.Article;
import article.service.ReadRecentArticleService;
import mvc.command.CommandHandler;

public class ReadRecentHandler implements CommandHandler {

	private ReadRecentArticleService recentService = new ReadRecentArticleService();

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		Article article = recentService.recent();
		
		if(article == null) {
			return "/WEB-INF/view/noArticle.jsp";
		}
		req.setAttribute("article", article);
		return "/WEB-INF/view/recentArticle.jsp";
	}
	
	
}
