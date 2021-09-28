package article.dao;
// jsp_crud 테이블에 데이터를 추가할 때 사용할 ArticleDao 클래스를 구현함

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import article.model.Article;
import jdbc.JdbcUtil;

public class ArticleDao {

	// 게시글 작성 관련 insert()메서드 구현
	public Article insert(Connection conn, Article article) throws SQLException {
		PreparedStatement pstmt = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			// article_id값이 자동으로 increment될 수 있도록 생성한 시퀀스를 이용해 각 칼럼의 데이터 값을
			// insert해줌(시퀀스명.NEXTVAL)
			pstmt = conn.prepareStatement("insert into jsp_crud(article_id, article_title, article_content) values(jsp_seq.NEXTVAL,'?','?')");
			pstmt.setString(2, article.getTitle());
			pstmt.setString(3, article.getContent());
			int insertedCount = pstmt.executeUpdate();
			
			if(insertedCount > 0) {
				stmt = conn.createStatement();
				rs = stmt.executeQuery("select jsp_seq.CURVAL from dual");
				if(rs.next()) {
					Integer newNum = rs.getInt(1);
					return new Article(newNum, article.getTitle(), article.getContent());
				}
			}
			return null;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(stmt);
			JdbcUtil.close(pstmt);
			
		}
	}
	
	public Article selectById(Connection conn, int no) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement("select * from (select * from jsp_crud order by rownum desc) where rownum=1");
			rs = pstmt.executeQuery();
			
			Article article = null;
			if(rs.next()) {
				article = convertArticle(rs);
			}
			return article;
		}finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
		
	}

	private Article convertArticle(ResultSet rs) throws SQLException {
		return new Article(rs.getInt("article_id"), rs.getString("article_title"), rs.getString("article_content"));
	}
}
