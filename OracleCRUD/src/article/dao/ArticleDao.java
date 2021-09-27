package article.dao;
// jsp_crud 테이블에 데이터를 추가할 때 사용할 ArticleDao 클래스를 구현함

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import article.model.Article;
import jdbc.JdbcUtil;

public class ArticleDao {

	private static ArticleDao articleDao = new ArticleDao();

	public static ArticleDao getInstance() {
		return articleDao;
	}

	private ArticleDao() {
	}

	// 게시글 작성 관련 insert()메서드 구현
	public int insert(Connection conn, Article article) throws SQLException {
		PreparedStatement pstmt = null;

		try {
			// article_id값이 자동으로 increment될 수 있도록 생성한 시퀀스를 이용해 각 칼럼의 데이터 값을
			// insert해줌(시퀀스명.NEXTVAL)
			pstmt = conn.prepareStatement("insert into jsp_crud values(jsp_seq.NEXTVAL,'?','?')");
			pstmt.setInt(1, article.getNumber());
			pstmt.setString(2, article.getTitle());
			pstmt.setString(3, article.getContent());
			return pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}
}
