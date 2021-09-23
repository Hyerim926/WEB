package article.service;

import java.sql.Connection;
import java.util.List;

import article.dao.ArticleDao;
import article.model.Article;
import jdbc.connection.ConnectionProvider;

public class ListArticleService {
	
	private ArticleDao articleDao = new ArticleDao();
	private int size = 10;
	
	public ArticlePage getArticlePage(int pageNum) {
		
	try(Connection conn = ConnectionProvider.getConnection()){
			// 전체 게시글의 개수를 구한다.
			int total = articleDao.selectCount(conn);
			
			// pageNum에 해당하는 게시글 목록을 구한다.
			// articleDao.select() 메서드의 두번째 파라미터인
			// (pageNum-1)*size 는 조회할 레코드의 시작 행이다.
			// 시작행은 0번 기준으로 (pageNum-1)*size를 시작 행 번호로 사용한다.
			// 예를들어, 3페이지를 요청하면 (3-1)*10인 20을 시작 행 번호로 사용한다.
			// 3페이지는 21번째 레코드부터 10개의 레코드를 구하므로,
			// 시작 행 번호는 20이 된다.
			List<Article> content = articleDao.select(conn,
					(pageNum-1)*size, size);
			
			// ArticlePage 객체를 리턴한다.
			return new ArticlePage(total, pageNum, size, content);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
