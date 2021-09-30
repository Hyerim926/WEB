package article.model;

//Article VO 클래스
public class Article {

	private Integer id; // jsp_crud 테이블의 article_id 필드(칼럼)와 연관
	private String title; // jsp_crud 테이블의 article_title 필드(칼럼)와 연관
	private String content; // jsp_crud 테이블의 article_content 필드(칼럼)와 연관

	public Article(Integer id, String title, String content) {
		this.id = id;
		this.title = title;
		this.content = content;
	}

	public Integer getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}
}
