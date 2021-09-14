package guestbook.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import guestbook.dao.MessageDao;
import guestbook.model.Message;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

public class GetMessageListService {

						  // 싱글톤 패턴 생성 및 활용
	private static GetMessageListService instance = new GetMessageListService();

			  // 싱글톤 패턴 생성 및 활용	
	public static GetMessageListService getInstance() {
		return instance;
	}
	
	private GetMessageListService() {
		
	}
	// 한 페이지에 보여줄 메시지 개수를 3으로 지정함
	private static final int MESSAGE_COUNT_PER_PAGE = 3;
	
	public MessageListView getMessageList(int pageNumber) {
		Connection conn = null;
		int currentPageNumber = pageNumber;
		try {
			conn = ConnectionProvider.getConnection();
			MessageDao messageDao = MessageDao.getInstance();
			
		    // MessageDao.selectCount()를 이용해서 전체 메시지 개수를 구한다. 
			int messageTotalCount = messageDao.selectCount(conn);
			
			List<Message> messageList = null;
			int firstRow = 0;
			int endRow = 0;
			if(messageTotalCount > 0) {
				// 메시지 개수가 0보다 크면 요청한 페이지에 속하는 메시지의 시작 행과 끝 행을 구한다.
				// 예를들어, 전체 메시지 개수가 7이고 요청한 페이지가 2인 경우,
				// 읽어올 메시지의 시작행 번호와 끝 행 번호는 각각 4와 6이 된다.
				firstRow =
						(pageNumber - 1) * MESSAGE_COUNT_PER_PAGE + 1;
				endRow = firstRow + MESSAGE_COUNT_PER_PAGE - 1;
				// messageDao.selectList()를 이용해서 시작행과 끝행에 속하는 메시지 목록을 구한다.
				messageList =
						messageDao.selectList(conn, firstRow, endRow);
				
			} else {
				// 메시지 개수가 0인 경우 emptyList(빈 List)를 messageList에 할당한다.
				currentPageNumber = 0;
				messageList = Collections.emptyList();
			}	   // MessageListView 객체를 리턴한다.
			return new MessageListView(messageList, messageTotalCount, currentPageNumber, MESSAGE_COUNT_PER_PAGE, firstRow, endRow);

		}catch(SQLException e) {
			throw new ServiceException("목록 구하기 실패 ㅠ.ㅠ : " + e.getMessage(), e);
			
		}finally {
			JdbcUtil.close(conn);
			
		}
		
	}
	
}
