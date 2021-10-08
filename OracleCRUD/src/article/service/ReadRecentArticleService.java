package article.service;

import java.sql.Connection;
import java.sql.SQLException;

import article.dao.ArticleDao;
import article.model.Article;
import jdbc.connection.ConnectionProvider;

public class ReadRecentArticleService {

	private ArticleDao articleDao = new ArticleDao();

	public Article recent() {
		try (Connection conn = ConnectionProvider.getConnection()) {
			Article article = articleDao.selectById(conn);
			if (article == null) {
				throw new ArticleNotFoundException();
			}
			return article;
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
}
