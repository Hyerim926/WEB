package article.command;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import article.service.ArticleData;
import article.service.ArticleNotFoundException;
import article.service.DeleteArticleService;
import article.service.DeleteRequest;
import article.service.ModifyArticleService;
import article.service.ModifyRequest;
import article.service.PermissionDeniedException;
import article.service.ReadArticleService;
import auth.service.User;
import mvc.command.CommandHandler;

public class DeleteArticleHandler implements CommandHandler {

	private static final String FORM_VIEW = "/WEB-INF/view/deleteForm.jsp";	
	
	private DeleteArticleService deleteService = new DeleteArticleService();	
	
	private ReadArticleService readService =
			new ReadArticleService();
	
	
	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		if (req.getMethod().equalsIgnoreCase("GET")) {
			return processForm(req, res);			
		} else if (req.getMethod().equalsIgnoreCase("POST")) {
			return processSubmit(req, res);
		} else {
			// 405 응답 코드 전송(허용되지 않는 메소드 응답)
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
			
			// 현재 로그인한 사용자가 게시글의 작성자가 아니면
			// 403 응답 전송(서버 응답 실행 거부)을 처리한다.
			if(!canDelete(authUser, articleData)) {
				// 403 응답 전송(서버 응답 실행 거부)
				res.sendError(HttpServletResponse.SC_FORBIDDEN);
				return null;
			}
			
			// 폼에 데이터를 보여줄 때 사용할 객체를 생성하고,
			// request의 modReq 속성에 저장한다.
			DeleteRequest delReq = new DeleteRequest(authUser.getId(), no);
			
			req.setAttribute("delReq", delReq);
			
			// 폼을 위한 뷰를 리턴한다.
			return FORM_VIEW;
		
			// 게시글이 존재하지 않으면
			// ArticleNotFoundException 예외 처리를 발생하고
		} catch (ArticleNotFoundException e) {
	
			// 404 응답 코드 전송(요청한 자원이 존재하지 않음)
			res.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
	}
	
// 앞서 Page 668 47행 ModifyArticleService의 canDelete() 메서드 비교 바람
	private boolean canDelete(User authUser, ArticleData articleData) {
		String writerId =articleData.getArticle().getWriter().getId(); 

		return authUser.getId().equals(writerId);
	}
	
			// 폼 전송을 처리한다.
	private String processSubmit(HttpServletRequest req, HttpServletResponse res) throws IOException {
		
		// 게시글 삭제를 요청한 사용자 정보를 구한다.
		User authUser = (User) req.getSession().getAttribute("authUser");
		String noVal = req.getParameter("no");
		int no = Integer.parseInt(noVal);
		
		// 요청 파라미터와 함께 현재 사용자 정보를 이용해서
		// DeleteRequest 객체를 생성한다.
		DeleteRequest delReq = new DeleteRequest(authUser.getId(), no);
		
		// DeleteRequest 객체를 request의 delReq 속성에 저장한다.
		req.setAttribute("delReq", delReq);
		
		try {
			// 게시글 삭제 기능을 실행한다.
			deleteService.delete(delReq);
			return "/WEB-INF/view/deleteSuccess.jsp";
			
			// ArticleNotFoundException 예외가 발생하면 익셉션을 처리한다.
		} catch (ArticleNotFoundException e) {

			// 404 응답 코드 전송(요청한 자원이 존재하지 않음)
			res.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
			
			// PermissionDeniedException 예외가 발생하면 익셉션을 처리한다.
		} catch (PermissionDeniedException e) {
			
			// 403 응답 전송(서버 응답 실행 거부)
			res.sendError(HttpServletResponse.SC_FORBIDDEN);
			return null;
		}
	}
}
