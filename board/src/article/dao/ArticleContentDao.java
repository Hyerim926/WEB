package article.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import article.model.Article;
import article.model.ArticleContent;
import jdbc.JdbcUtil;

// ArticleContentDao 클래스의 insert() 메서드를 다음과 같이 선언한다.
// insert 쿼리 실행에 성공하면 파라미터로 전달받은 content 객체를 그대로 리턴하고
// 아니면 null을 리턴한다.
public class ArticleContentDao {

	public ArticleContent insert(Connection conn, ArticleContent content) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement("insert into article_content(article_no,content)\r\n" + 
					" values (?,?)");
			pstmt.setLong(1, content.getNumber());
			pstmt.setString(2, content.getContent());
			int insertedCount = pstmt.executeUpdate();
			
			if(insertedCount > 0) {
				return content;
			} else {
				return null;
			}
		} finally {
			JdbcUtil.close(pstmt);
		}
	}
	
	// ArticleContent 클래스의 조회 관련 selectById() 메서드 구현
	public ArticleContent selectById(Connection conn, int no) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = conn.prepareStatement("select * from article_content where article_no = ?");
			pstmt.setInt(1, no);
			rs = pstmt.executeQuery();
			ArticleContent content = null;
			if(rs.next()) {
				content  = new ArticleContent(rs.getInt("article_no"), rs.getString("content"));
			}
			return content;

		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}
	
}
