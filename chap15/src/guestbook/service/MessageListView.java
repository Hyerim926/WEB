package guestbook.service;
// 메시지 목록 보기(MessageListView) 클래스 생성

import java.util.List;

import guestbook.model.Message;

public class MessageListView {

	private int messageTotalCount; // 메시지 전체 개수
	private int currentPageNumber; // 요청 페이지 번호
	
	private List<Message> messageList; // 메시지 리스트
	private int pageTotalCount;
	private int messageCountPerPage; // 페이지 당 메시지 출력 개수
	private int firstRow; // 전체 메시지 중에서 현재 메시지 목록의 시작 행(startRow)
	private int endRow; // 전체 메시지 중에서 현재 메시지 목록의 끝 행(endRow)
	
	public MessageListView(List<Message> messageList, int messageTotalCount, int currentPageNumber,
			int messageCountPerPage, int startRow, int endRow) {
		
		this.messageTotalCount = messageTotalCount;
		this.currentPageNumber = currentPageNumber;
		this.messageList = messageList;
		this.messageCountPerPage = messageCountPerPage;
		this.firstRow = startRow;
		this.endRow = endRow;
		
		calculatePageTotalCount();
	}

	private void calculatePageTotalCount() {
		if(messageTotalCount == 0) {
			pageTotalCount = 0;
		}else {
			pageTotalCount = messageTotalCount / messageCountPerPage;
			if(messageTotalCount % messageCountPerPage > 0) {
				pageTotalCount++;
			}
		}
	}

	public int getMessageTotalCount() {
		return messageTotalCount;
	}

	public int getCurrentPageNumber() {
		return currentPageNumber;
	}

	public List<Message> getMessageList() {
		return messageList;
	}

	public int getPageTotalCount() {
		return pageTotalCount;
	}

	public int getMessageCountPerPage() {
		return messageCountPerPage;
	}

	public int getFirstRow() {
		return firstRow;
	}

	public int getEndRow() {
		return endRow;
	}
	
	public boolean isEmpty() {
		return messageTotalCount == 0;
	}
	
	
	
	
}
