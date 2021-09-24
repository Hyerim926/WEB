package article.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import article.model.Article;
import article.model.Writer;
import jdbc.JdbcUtil;

// article 테이블과 article_content 테이블에 데이터를 추가할 때
// 사용할 ArticleDao 클래스와 ArticleContentDao 클래스를 구현한다.
public class ArticleDao {
	public Article insert(Connection conn, Article article) throws SQLException {
		PreparedStatement pstmt = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			// insert 쿼리를 실행해서 article 테이블에 데이터를 삽입한다.
			// article_no 칼럼은 자동 증가 칼럼이르모
			// insert 쿼리를 실행할 때 값을 지정하지 않는다.
			pstmt = conn.prepareStatement("insert into article(writer_id, writer_name,\r\n"
					+ "title, regdate, moddate, read_cnt) values(?,?,?,?,?,0)");

			pstmt.setString(1, article.getWriter().getId());
			pstmt.setString(2, article.getWriter().getName());
			pstmt.setString(3, article.getTitle());
			pstmt.setTimestamp(4, toTimestamp(article.getRegDate()));
			pstmt.setTimestamp(5, toTimestamp(article.getModifiedDate()));
			int insertedCount = pstmt.executeUpdate();

			if (insertedCount > 0) {
				stmt = conn.createStatement();
				// 앞서 article 테이블에 새롭게 추가한 행의 article_no값을 구한다.
				// 이 쿼리를 통해 article_no 칼럼의 값을 구할 수 있다.
				rs = stmt.executeQuery("select last_insert_id() from article");

				if (rs.next()) {
					// 앞서 41행에서 실행한 쿼리문으로 구한 신규 게시글의 번호를 구한다.
					Integer newNum = rs.getInt(1);
					// article 테이블에 추가한 데이터를 담은 Article 객체를 리턴한다.
					return new Article(newNum, article.getWriter(), article.getTitle(), article.getRegDate(),
							article.getModifiedDate(), 0);
				}
			}
			return null;

		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(stmt);
			JdbcUtil.close(pstmt);
		}
	}

	private Timestamp toTimestamp(Date date) {
		return new Timestamp(date.getTime());
	}

	// 게시글 목록을 표현하려면 다음의 두 데이터를 가져올 수 있어야 한다.
	// 1) 페이지 개수를 구하기 위한 전체 게시글 개수
	// 2) 지정한 행 번호에 해당하는 게시글 목록
	// 상기 두 데이터를 가져오는데 필요한 메서드를 ArticleDao에 추가한다.
	// 먼저, 전체 개시글 개수를 구하기 위한 selectCount() 메서드를 구현한다.
	// selectCount() 메서드는 article 테이블의 전체 레코드 수를 리턴한다.
	public int selectCount(Connection conn) throws SQLException {
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select count(*) from article");
			if (rs.next()) {
				return rs.getInt(1);
			}
			return 0;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(stmt);
		}
	}

	// 지정한 범위의 게시글을 읽어 오기 위한 select() 메서드를 구현한다.
	public List<Article> select(Connection conn, int startRow, int size) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			// Page 648 MySQL limit 쿼리 실행 메모 참고 바람
			pstmt = conn.prepareStatement("select * from article order by article_no desc limit ?, ?");

			// 첫 번째 시작 행 인덱스 번호 추출
			pstmt.setInt(1, startRow);

			// 첫 번째 시작 행 인덱스 번호 기준으로
			// 읽어올 레코드 개수 추출
			pstmt.setInt(2, size);
			rs = pstmt.executeQuery();
			List<Article> result = new ArrayList<Article>();
			while (rs.next()) {
				result.add(convertArticle(rs));
			}

			return result;

		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// convertArticle() 메서드는 ResultSet에서 데이터를 읽어와서
	// Article 객체를 생성한다.
	private Article convertArticle(ResultSet rs) throws SQLException {
		return new Article(rs.getInt("article_no"), new Writer(rs.getString("writer_id"), rs.getString("writer_name")),
				rs.getString("title"), toDate(rs.getTimestamp("regdate")), toDate(rs.getTimestamp("moddate")),
				rs.getInt("read_cnt"));
	}

	private Date toDate(Timestamp timestamp) {
		return new Date(timestamp.getTime());
	}

	// 게시글 조회 기능을 구현하려면 ArticleDao 클래스에 다음의 두 기능을 추가해야 한다.
	// 1) 특정 번호에 해당하는 게시글 데이터 읽기
	// 2) 특정 번호에 해당하는 게시글 데이터의 조회수 증가하기
	// 앞서 두가지 기능을 구현하기 위해 selectById() 메서드를 구현한다.
	public Article selectById(Connection conn, int no) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = conn.prepareStatement("select * from article where article_no = ?");
			pstmt.setInt(1, no);
			rs = pstmt.executeQuery();
			Article article = null;
			if (rs.next()) {
				// 참고로, convertArticle() 메서드는
				// 앞서 게시글 목록 조회 기능을 구현할 때 추가했었음
				article = convertArticle(rs);
			}
			return article;

		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 조회수를 1씩 증가시킬 때 사용하는 increaseReadCount() 메서드 구현
	public void increaseReadCount(Connection conn, int no) throws SQLException {
		try (PreparedStatement pstmt = conn
				.prepareStatement("update article set read_cnt = read_cnt + 1 " + "where article_no = ?")) {
			pstmt.setInt(1, no);
			pstmt.executeUpdate();
		}
	}

	// ArticleDao의 update() 메서드는 파라미터로 전달받은 게시글 번호와 제목을 이용해서 데이터 수정
	public int update(Connection conn, int no, String title) throws SQLException {
		// update 쿼리처럼 moddate 칼럼의 값을 현재시간으로 설정해서 데이터 최근 수정 시간을 기록함
		try (PreparedStatement pstmt = conn
				.prepareStatement("update article set title = ?, moddate = now() where article_no=?")) {
			pstmt.setString(1, title);
			pstmt.setInt(2, no);
			return pstmt.executeUpdate();
		}
	}

	public int delete(Connection conn, int no) throws SQLException {
		// update 쿼리처럼 moddate 칼럼의 값을 현재시간으로 설정해서 데이터 최근 수정 시간을 기록함
		try (PreparedStatement pstmt = conn
				.prepareStatement("delete from article where article_no=?")) {
			pstmt.setInt(1, no);
			return pstmt.executeUpdate();
		}
	}
	
	

}
