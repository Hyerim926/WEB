package article.model;

import java.util.Map;

// Article VO 클래스
public class Article {

	private Integer number; // jsp_crud 테이블의 article_id 필드(칼럼)와 연관
	private String title; // jsp_crud 테이블의 article_title 필드(칼럼)와 연관
	private String content; // jsp_crud 테이블의 article_content 필드(칼럼)와 연관

	public Article(Integer number, String title, String content) {
		this.number = number;
		this.title = title;
		this.content = content;
	}

	public Integer getNumber() {
		return number;
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
