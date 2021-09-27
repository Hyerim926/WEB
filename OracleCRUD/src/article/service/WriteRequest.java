package article.service;

import java.util.Map;

public class WriteRequest {

	private String title;
	private String content;

	public WriteRequest(String title, String content) {
		this.title = title;
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}

	// 데이터가 유효한지 여부 검사, 잘못된 데이터가 존재하면 errors 맵 객체에 관련 코드 추가
	public void validate(Map<String, Boolean> errors) {
		if (title == null || title.trim().isEmpty()) {
			errors.put("title", Boolean.TRUE);
		}
	}

}
