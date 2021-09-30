package article.service;

import java.sql.Connection;
import java.sql.SQLException;

import article.dao.ArticleDao;
import article.model.Article;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

//게시글 작성 기능을 제공한 WriteService 클래스 구현
//나의 경우 writeRequest를 활용하지 않았고, insert()메서드를 정확한 방법으로 활용하지 않았음
public class WriteService {

	private ArticleDao articleDao = new ArticleDao();

	// writeArticle() 메서드는 WriteRequest 타입의 req 매개변수를 이용해
	// 게시글을 등록하고, 결과로 게시글 번호를 리턴함(그래서 메서드명 앞에 Integer타입으로 선언된 것이며
	// 리턴값 또한 int형이어야함)
	public Integer write(WriteRequest req) {

		Connection conn = null;

		try {
			// 커넥션 싱글톤 생성
			conn = ConnectionProvider.getConnection();

			// 트랜잭션 처리를 위한 자동 커밋 false 처리
			conn.setAutoCommit(false);

			// req는 WriteRequest의 객체로서 title과 content 변수를 가지고 있음
			Article article = toArticle(req);
			// 같은 Article 타입의 savedArticle 변수에는 메서드 밖에서 private형으로 생성한 객체로
			// insert()메서들 호출해 바로 위에서 선언한 변수 article를 매개변수로 넣어줌
			Article savedArticle = articleDao.insert(conn, article);
			// savedArticle 변수가 null값일 때는 쿼리문이 실행이 안되며
			// jsp_crud 테이블에 삽입된 레코드가 없기에 예외 처리를 수행함
			if (savedArticle == null) {
				throw new RuntimeException("fail to insert article 입력 실패");
			}

			// 트랜잭션을 커밋 처리한다.
			conn.commit();

			// savedArticle.getId()로 가장 최근에 추가한 게시글 번호를 리턴함
			// insert()메서드로 쿼리문 실행했을 때 id값은 seq에 의해 자동 지정되므로 
			// dao클래스에서 setInt로 값을 지정해주지 않았더라도 getId로 값을 불러올 수 있음
			return savedArticle.getId();

			// 앞서 트랜잭션 처리 과정에서 예외처리 발생 시 트랜잭션 처리를 중단하고 rollback 처리함
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

	// WriteRequest 객체에서 가져온 title과 content값을 매개변수값으로 가지는 새  Article객체
	// id에 들어가는 값은 앞서 WriteReqeust 객체에서 설정해주지 않았기 때문에 null로 지정함
	// WriteRequest는 view에서 가져온 값들을 가지고 있는 객체임!
	private Article toArticle(WriteRequest req) {
		return new Article(null, req.getTitle(), req.getContent());
	}
}
