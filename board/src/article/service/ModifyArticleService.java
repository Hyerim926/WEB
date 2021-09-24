package article.service;

import java.sql.Connection;
import java.sql.SQLException;

import article.dao.ArticleContentDao;
import article.dao.ArticleDao;
import article.model.Article;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

// ModifyRequest를 이용해서 게시글 수정 기능을 제공하는 ModifyArticleService 클래스 제작
public class ModifyArticleService {

	// 이 클래스에서 사용할 객체 생성
	private ArticleDao articleDao = new ArticleDao();
	private ArticleContentDao contentDao = new ArticleContentDao();

	public void modify(ModifyRequest modReq) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			// 트랜잭션 시작을 위한 오토 커밋 false 설정
			conn.setAutoCommit(false);

			// 게시글 번호에 해당하는 Article 객체 생성
			Article article = articleDao.selectById(conn, modReq.getArticleNumber());
			// 해당번호를 가진 게시글이 존재하지 않을 경우 ArticleNotFoundException 예외 처리
			if (article == null) {
				throw new ArticleNotFoundException();
			}
			// 수정하려는 사용자가 해당 게시글에 대한 접근 권한이 있는지 검사
			// canModify()값이 false라면 PermissionDeniedException 예외 처리
			if (!canModify(modReq.getUserId(), article)) {
				throw new PermissionDeniedException();
			}
			
			// ArticleDao와 ArticleContentDao의 update() 메서드를 이용해 제목과 내용 수정
			articleDao.update(conn, modReq.getArticleNumber(), modReq.getTitle());
			contentDao.update(conn, modReq.getArticleNumber(), modReq.getContent());
			conn.commit();
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException(e);
		} catch (PermissionDeniedException e) {
			JdbcUtil.rollback(conn);
			throw e;
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// canModify() 메서드는 게시글을 수정할 수 있는지 검사하는 기능을 구현
	// 수정하려는 사용자 ID가 게시글 작성자 ID와 동일하면 true를 리턴한다
	private boolean canModify(String modifyingUserId, Article article) {
		return article.getWriter().getId().equals(modifyingUserId);
	}
}
