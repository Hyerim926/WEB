package article.service;

import java.sql.Connection;
import java.sql.SQLException;

import article.dao.ArticleDao;
import article.model.Article;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

// 게시글 작성 기능을 제공한 WriteService 클래스 구현
public class WriteService {

	private static WriteService instance = new WriteService();

	public static WriteService getInstance() {
		return instance;
	}

	private WriteService() {
	}

	public void write(Article article) {
		Connection conn = null;

		try {
			// 커넥션 싱글톤 생성
			conn = ConnectionProvider.getConnection();

			ArticleDao articleDao = ArticleDao.getInstance();
			articleDao.insert(conn, article);

		} catch (SQLException e) {
			throw new ServiceException("게시글 등록 실패: " + e.getMessage(),e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

}
