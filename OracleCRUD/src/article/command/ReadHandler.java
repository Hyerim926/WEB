package article.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import article.service.ArticleContentNotFoundException;
import article.service.ArticleData;
import article.service.ArticleNotFoundException;
import article.service.ReadRecentService;
import mvc.command.CommandHandler;

public class ReadHandler implements CommandHandler{

	private ReadRecentService readService = new ReadRecentService();

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String noVal = req.getParameter("article_id");
		int articleNum = Integer.parseInt(noVal);
		
		try {
			// readService.getArticle() 메서드로 AtricleData 객체 생성
			ArticleData articleData = readService.getArticle(articleNum);
			
			// request의 articleData 속성에 게시글 저장
			req.setAttribute("articleData", articleData);
			return "/WEB-INF/view/readRecent.jsp";
			
		// 예외 처리
		} catch (ArticleNotFoundException | ArticleContentNotFoundException e) {
			// 로그 메시지 기록
			req.getServletContext().log("no article", e);
			
			// 404 에러 전송
			res.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
	}
}
