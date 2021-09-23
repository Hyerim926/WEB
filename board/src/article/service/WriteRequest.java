package article.service;

import java.util.Map;

import article.model.Writer;

// 게시글을 쓰려면 세 개의 데이터(작성자, 제목, 내용)이 필요한데,
// 이 데이터를 전달할 때 사용할 WriteRequest 클래스를 제작한다.
public class WriteRequest {
	
	// 게시글을 쓰는데 필요한 작성자, 제목, 내용 데이터를 정의한다.
	private Writer writer;  // 작성자
	private String title;   // 제목
	private String content; // 내용
	
	public WriteRequest(Writer writer, String title, String content) {
		this.writer = writer;
		this.title = title;
		this.content = content;
	}

	public Writer getWriter() {
		return writer;
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}
	
	// 데이터가 유효한지 여부를 검사한다.
	// 잘못된 데이터가 존재하면 errors 맵 객체에 관련 코드를 추가한다.
	public void validate(Map<String, Boolean> errors) {
		if(title == null || title.trim().isEmpty()) {
			errors.put("title", Boolean.TRUE);
		}
	}
}
