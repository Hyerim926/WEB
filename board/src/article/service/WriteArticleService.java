package article.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import article.dao.ArticleContentDao;
import article.dao.ArticleDao;
import article.model.Article;
import article.model.ArticleContent;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

// 게시글 쓰기 기능을 제공할 WriteArticleService 클래스를 제작한다.
public class WriteArticleService {
	
	private ArticleDao articleDao = new ArticleDao();
	private ArticleContentDao contentDao = new ArticleContentDao();
	
	// write() 메서드는 WriteRequest 타입의 req 파라미터(매개변수)를 이용해서
	// 게시글을 등록하고, 결과로 게시글 번호를 리턴한다.
	public Integer write(WriteRequest req) {
		
		Connection conn = null;
		
		try {
			conn = ConnectionProvider.getConnection();
			// 트랜잭션 처리를 위한 자동 커밋 false 처리
			conn.setAutoCommit(false);
			
			// WriteRequest로부터 Article 객체를 생성한다.
			Article article = toArticle(req);
			
			// ArticleDao의 insert() 메서드를 실행하고,
			// 그 결과를 savedArticle에 할당한다.
			// 데이터를 삽입한 경우 savedArticle은 null이 아니고,
			// article 테이블에 추가한 데이터의 주요키값을
			// number 값으로 갖는다.
			Article savedArticle = articleDao.insert(conn, article);
			
			// savedArticle이 null이면
			// article 테이블에 삽입한 레코드가 없기에
			// RuntimeException 예외 처리를 수행한다.
			if(savedArticle == null) {
				throw new RuntimeException("fail to insert article 입력 실패");
			}
			
			// ArticleContent 객체를 생성한다.
			// 이때, savedArticle의 게시글 번호를 사용한다.
			// 앞서 insert 데이터와 동일한 번호를 값으로 갖는
			// ArticleContent 객체를 생성한다.
			ArticleContent content = new ArticleContent(savedArticle.getNumber(), req.getContent());
			
			// ArticleContentDao의 insert() 메서드를 활용해서
			// article_content 테이블에 데이터를 삽입한다.
			ArticleContent savedContent = contentDao.insert(conn, content);
			
			// savedContent이 null 이면 article_content 테이블에
			// 삽입한 레코드가 없기에 RuntimeException 예외 처리를 수행한다.
			if(savedContent == null) {
				throw new RuntimeException("fail to insert article_content 입력 실패");
			}
			
			// 트랜잭션을 커밋 처리한다.
			conn.commit();
			
			// savedArticle.getNumber()로 새로 추가한 게시글 번호를 리턴한다.
			return savedArticle.getNumber();
			
			// 앞서 트랜잭션 처리 과정에서 예외 처리가 발생할 경우
			// 트랜잭션 처리를 중단하고 롤백 처리를 한다.
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException();
		} catch (RuntimeException e) {
			JdbcUtil.rollback(conn);
			throw e;
		} finally {
			JdbcUtil.close(conn);
		}
	}

	private Article toArticle(WriteRequest req) {
		Date now = new Date();
		return new Article(null, req.getWriter(), req.getTitle(), now, now, 0);
	}
}
