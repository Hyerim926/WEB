package article.model;

public class ArticleContent {

	// article_content 테이블의 article_no 필드(칼럼)와 연관됨
	private Integer number; 

	// article_content 테이블의 content 필드(칼럼)와 연관됨
	private String content;
	
	public ArticleContent(Integer number, String content) {
		this.number = number;
		this.content = content;
	}

	public Integer getNumber() {
		return number;
	}

	public String getContent() {
		return content;
	}

}
