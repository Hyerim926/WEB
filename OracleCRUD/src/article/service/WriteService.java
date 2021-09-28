package article.service;

import java.sql.Connection;
import java.sql.SQLException;

import article.dao.ArticleDao;
import article.model.Article;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

// 게시글 작성 기능을 제공한 WriteService 클래스 구현
public class WriteService {

	private ArticleDao articleDao = new ArticleDao();

	public Integer write(WriteRequest req) {
		Connection conn = null;

		try {
			// 커넥션 싱글톤 생성
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			
			// WriteRequest로부터 Article 객체 생성함
			Article article = toArticle(req);

			// ArticleDao의 insert()메서드를 실행하고 그 결과를 newArticle 할당함
			// 데이터 삽입한 경우 newArticle은 null이 아니고 
			// jsp_crud 테이블에 추가한 데이터의 주요키값을 number값으로 갖는다
			Article newArticle = articleDao.insert(conn, article);
			// newArticle이 null이면 jsp_crud 테이블에 삽입된 레코드가 없기에 RuntimeException 예외 처리를 수행한다
			if (newArticle == null) {
				throw new RuntimeException("fail to insert article");
			}

			// 트랜잭션 커밋 처리
			conn.commit();

			// newArticle.getNumber()로 새로 추가한 게시글 번호 리턴함
			return newArticle.getNumber();
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
		return new Article(null, req.getTitle(), req.getContent());
	}

}
