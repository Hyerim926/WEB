package article.command;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import article.service.ArticleData;
import article.service.ArticleNotFoundException;
import article.service.ModifyArticleService;
import article.service.ModifyRequest;
import article.service.PermissionDeniedException;
import article.service.ReadArticleService;
import auth.service.User;
import mvc.command.CommandHandler;

// ModifyArticleHandler 클래스는 다음과 같이 동작한다
// GET 방식: 수정할 게시글 데이터를 읽어와 폼에 보여줌
// POST 방식: 전송한 요청 파라미터를 이용해 게시글 수정함. 파라미터 값이 잘못된 경우,
// 전송한 데이터를 이용해서 폼을 다시 보여줌
// 앞전까지의 핸들러는 GET방식에 대해 폼을 출력할 떄, 폼에 데이터를 채우지 않았는데
// 지금의 경우 GET방식으로 폼을 요청할 때, 수정할 데이터 내용을 폼에 채워서 보여줘야함
// 따라서, ModifyArticleHandler는 ModifyArticleService뿐만 아니라 ReadArticleService를 함께 사용함
public class ModifyArticleHandler implements CommandHandler {

	private static final String FORM_VIEW = "/WEB-INF/view/modifyForm.jsp";

	private ReadArticleService readService = new ReadArticleService();
	private ModifyArticleService modifyService = new ModifyArticleService();

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		if (req.getMethod().equalsIgnoreCase("GET")) {
			return processForm(req, res);
		} else if (req.getMethod().equalsIgnoreCase("POST")) {
			return processSubmit(req, res);
		} else {
			res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return null;
		}
	}

	// 폼 요청을 처리함
	private String processForm(HttpServletRequest req, HttpServletResponse res) throws IOException {
		try {
			String noVal = req.getParameter("no");
			int no = Integer.parseInt(noVal);

			// 폼에 보여줄 게시글을 구한다.
			ArticleData articleData = readService.getArticle(no, false);
			// 현재 로그인한 사용자 정보를 구한다.
			User authUser = (User) req.getSession().getAttribute("authUser");
			// 현재 로그인한 사용자가 게시글의 작성자가 아니면 403 응답 전송(서버 응답 실행 거부)을 처리함
			if (!canModify(authUser, articleData)) {
				// 403 응답 전송(서버 응답 실행 거부)
				res.sendError(HttpServletResponse.SC_FORBIDDEN);
				return null;
			}
			
			// 폼에 데이터를 보여줄 때 사용할 객체를 생성하고 request의 modReq 속성에 저장함
			ModifyRequest modReq = new ModifyRequest(authUser.getId(), no, articleData.getArticle().getTitle(),
					articleData.getContent());

			req.setAttribute("modReq", modReq);
			// 폼을 위한 뷰 리턴
			return FORM_VIEW;
			
			// 게시글이 존재하지 않으면 ArticleNotFoundException 예외 처리 발생
		} catch (ArticleNotFoundException e) {
			// 404 응답 코드 전송(요청한 자원이 존재하지 않음)
			res.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
	}

	// 앞서 Page 668 47행 ModifyArticleService 클래스의 canModify()메서드 비교 바람
	private boolean canModify(User authUser, ArticleData articleData) {
		String writerId = articleData.getArticle().getWriter().getId();
		return authUser.getId().equals(writerId);
	}

	// 폼 전송을 처리한다
	private String processSubmit(HttpServletRequest req, HttpServletResponse res) throws IOException {
		// 게시글 수정을 요청한 사용자 정보를 구한다
		User authUser = (User)req.getSession().getAttribute("authUser");
		String noVal = req.getParameter("no");
		int no = Integer.parseInt(noVal);
		
		// 요청 파라미터와 함께 현재 사용자 정보를 이용해 ModifyRequest 객체 생성
		ModifyRequest modReq = new ModifyRequest(authUser.getId(), no, 
				req.getParameter("title"), req.getParameter("content"));
		// ModifyRequestr객체를 request의 modReq 속성에 저장
		req.setAttribute("modReq", modReq);
		
		// 에러 정보를 담을 맵 객체 생성
		Map<String, Boolean> errors = new HashMap<>();
		// ModifyRequest 객체가 유효한지 검사한다
		req.setAttribute("errors", errors);
		modReq.validate(errors);
		// 앞서 유효성 검사에서 유효하지 않은 데이터가 존재한다면
		if(!errors.isEmpty()) {
			// 다시 폼을 보여준다.
			return FORM_VIEW;
		}
		try {
			// 게시글 수정 기능 실행
			modifyService.modify(modReq);
			// 수정에 성공하면 modifySuccess.jsp 보여줌
			return "/WEB-INF/view/modifySuccess.jsp";
			// ArticleNotFoundException 예외 발생 시 익셉션 처리
		} catch (ArticleNotFoundException e) {
			// 404 응답 코드 전송(요청한 자원이 존재하지 않음)
			res.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
			// PermissionDeniedException 예외 발생 시 익셉션 처리
		} catch (PermissionDeniedException e) {
			// 403 응답 코드 전송(서버 응답 실행 거부)
			res.sendError(HttpServletResponse.SC_FORBIDDEN);
			return null;
		}
	}

}
