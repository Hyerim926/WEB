package article.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import article.service.ArticlePage;
import article.service.ListArticleService;
import mvc.command.CommandHandler;

// 웹 요청을 처리하는 ListArticleHandler 클래스 제작
public class ListArticleHandler implements CommandHandler {
	
	private ListArticleService listService = new ListArticleService();

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		// pageNo 파라미터 값을 이용해서 읽어올 페이지 번호를 구한다.
		String pageNoVal = req.getParameter("pageNo");
		int pageNo = 1;
		if(pageNoVal != null) {
			pageNo = Integer.parseInt(pageNoVal);
		}
		// 지정한 페이지 번호에 해당하는 게시글 데이터를 구한다.
		ArticlePage articlePage = listService.getArticlePage(pageNo);
		
		// ArticlePage 객체를 JSP에서 사용할 수 있도록
		// request의 articlePage 속성에 저장한다.
		req.setAttribute("articlePage", articlePage);
		return "/WEB-INF/view/listArticle.jsp";
	}
}  
