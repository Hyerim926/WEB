package article.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import article.service.ArticleContentNotFoundException;
import article.service.ArticleData;
import article.service.ArticleNotFoundException;
import article.service.ReadArticleService;
import mvc.command.CommandHandler;

public class ReadArticleHandler implements CommandHandler {

	private ReadArticleService readService = new ReadArticleService();
	
	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String noVal = req.getParameter("no");
		int articleNum = Integer.parseInt(noVal);
		try {
		// readService.getArticle() 메서드로 ArticleData 객체를 생성한다.
		ArticleData articleData = readService.getArticle(articleNum, true);
		
		// request의 articleData 속성에 게시글을 저장한다.
		req.setAttribute("articleData", articleData);
		return "/WEB-INF/view/readArticle.jsp";

		// 게시글이 존재하지 않아서, 예외 처리 ArticleNotFoundException 익셉션
		// 또는 ArticleContentNotFoundexception 익셉션이 발생한다면
		} catch (ArticleNotFoundException | ArticleContentNotFoundException e) {
		
		// 로그 메시지를 기록하고
		req.getServletContext().log("no article", e);
		
		// HttpServletResponse.SC_NOT_FOUND : 404 에러 코드를 전송한다.
		res.sendError(HttpServletResponse.SC_NOT_FOUND);
		return null;
		}
	}
}
