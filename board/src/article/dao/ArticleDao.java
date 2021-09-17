package article.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

import article.model.Article;
import jdbc.JdbcUtil;

// article 테이블과 article_content 테이블에 데이터를 추가할 떄
// 사용할 ArticleDao 클래스와 ArticleContentDao 클래스를 구현함
public class ArticleDao {

	public Article insert(Connection conn, Article article) throws SQLException {
		PreparedStatement pstmt = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			// insert 쿼리를 실행해서 article 테이블에 데이터를 삽입한다. article_no 칼럼은 자동 증가 칼럼이므로
			// insert 쿼리 실행시 값 지정 않함
			pstmt = conn
					.prepareStatement("insert into article(writer_id, writer_name, title, regdate, moddate, read_cnt)"
							+ "values(?,?,?,?,?,0)");
			pstmt.setString(1, article.getWriter().getId());
			pstmt.setString(2, article.getWriter().getName());
			pstmt.setString(3, article.getTitle());
			pstmt.setTimestamp(4, toTimestamp(article.getRegdate()));
			pstmt.setTimestamp(5, toTimestamp(article.getModifiedDate()));
			int insertedCount = pstmt.executeUpdate();

			if (insertedCount > 0) {
				stmt = conn.createStatement();
				// 앞서 article 테이블에 새롭게 추가한 행의 article_no값을 구한다
				// 이를 통해 article_no 칼럼값 구할 수 있음
				rs = stmt.executeQuery("select last_insert_id() from article");
				if (rs.next()) {
					// 앞서 39행에서 실행한 쿼리문으로 구한 신규 게시글의 번호를 구한다
					Integer newNum = rs.getInt(1);
					// article 테이블에 추가한 데이터를 담은 Article 객체 리턴
					return new Article(newNum, article.getWriter(), article.getTitle(), article.getRegdate(),
							article.getModifiedDate(), 0);
				}
			}
			return null;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(stmt);
			JdbcUtil.close(conn);
		}
	}

	private Timestamp toTimestamp(Date date) {
		return new Timestamp(date.getTime());
	}
}
