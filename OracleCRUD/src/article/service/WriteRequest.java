package article.service;

import java.util.Map;

//신규 게시글 등록 시 필요한 데이터 제공함
//view와 service(db)의 가교 역할이라고 여기면 됨
public class WriteRequest {

	private String title; // 제목
	private String content; // 내용

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
	// title이 null값이거나 여백을 잘라내고도 값이 공백일 떄 errors 객체에 값을 추가함
	public void validate(Map<String, Boolean> errors) {
		if (title == null || title.trim().isEmpty()) {
			errors.put("title", Boolean.TRUE);
		}
	}
}
