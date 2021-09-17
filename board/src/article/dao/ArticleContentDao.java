package article.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import article.model.ArticleContent;
import jdbc.JdbcUtil;

// ArticleContentDao 클래스의 insert() 메서드를 다음과 같이 선언함
// insert 쿼리 실행에 성공하면 파라미터로 전달받은 content 객체를 그대로 리턴하고, 아니면 null을 리턴함
public class ArticleContentDao {

	public ArticleContent insert(Connection conn, ArticleContent content) throws SQLException {
		PreparedStatement pstmt = null;

		try {
			pstmt = conn.prepareStatement("insert into article_content(article_no, content)" + "values(?,?)");
			pstmt.setLong(1, content.getNumber());
			pstmt.setString(2, content.getContent());
			int insertedCount = pstmt.executeUpdate();
			// update된 개수가 0이상이면 content를 리턴하고 아니면 null을 리턴함
			if (insertedCount > 0) {
				return content;
			} else {
				return null;
			}
		} finally {
			JdbcUtil.close(pstmt);
		}
	}
}
