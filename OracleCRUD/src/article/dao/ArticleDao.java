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
		
		// JDBC 관련 변수을 null값으로 선언함
		PreparedStatement pstmt = null;
		Statement stmt = null;
		ResultSet rs = null;


		try {
			// 쿼리문 실행을 대기함
			// article_id값이 자동으로 increment될 수 있도록 생성한 시퀀스를 이용해 각 칼럼의 데이터 값을
			// insert해줌(시퀀스명.nextval)
			pstmt = conn.prepareStatement("insert into jsp_crud values(idx_seq.nextval, ?, ?)");
			
			// 쿼리문의 빈 값에 article 객체로부터 title과 content를 받아와서 넣어줌
			pstmt.setString(1, article.getTitle());
			pstmt.setString(2, article.getContent());
			
			// 나의 경우, 하단 로직을 짜지 않아서 View에서 DB로 값이 저장되지 않았던 것이었음
			
			// executeUpdate()된 횟수를 insertedCount라는 변수로 설정함
			int insertedCount = pstmt.executeUpdate();
			
			// 만약 insertedCount값이 0보다 크다면 즉, 쿼리문이 한 개 이상 실행됐을 때
			if(insertedCount > 0) {
				// createStatement()메서드로 DB로 SQL문을 보내기 위한 SQLServerStatement 개체를 만듦
				stmt = conn.createStatement();
				// rs 변수에 article_id가 가장 큰 값(=가장 최근에 들어간 값)에 해당하는 데이터를 조회하는 쿼리문을 실행해줌
				rs = stmt.executeQuery("select * from jsp_crud where article_id = (select max(article_id) from jsp_crud");
				// 위 쿼리문을 실행해서 저장된 rs의 값을 next()메서드를 이용해 꺼냈을 때
				if(rs.next()) {
					// Article insert() 메서드의 return값으로는 rs(저장된 DB)로부터 가져온 각 칼럼값들로 지정해줌
					return new Article(rs.getInt("ARTICLE_ID"), rs.getString("ARTICLE_TITLE")
							, rs.getString("ARTICLE_CONTENT"));
				}
			}
			return null;
		} finally {
			JdbcUtil.close(pstmt);

		}
	}

//	public void selectById(Connection conn, int no) throws SQLException {
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		try {
//			pstmt = conn.prepareStatement("select * from (select * from jsp_crud order by rownum desc) where rownum=1");
//			rs = pstmt.executeQuery();
//			
//			Article article = null;
//			if(rs.next()) {
//				article = convertArticle(rs);
//			}
//		}finally {
//			JdbcUtil.close(rs);
//			JdbcUtil.close(pstmt);
//		}
//		
//	}
//
//	private Article convertArticle(ResultSet rs) throws SQLException {
//		return new Article(rs.getInt("article_id"), rs.getString("article_title"), rs.getString("article_content"));
//	}
}