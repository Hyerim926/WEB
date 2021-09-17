package article.model;

// article 테이블과 article_content 테이블의 데이터를 자바 코드에서
// 표현하기 위해 Article 클래스, Writer 클래스, ArticleContent 클래스를 사용할 것이다
// 먼저 Writer 클래스는 게시글 작성자 정보를 담는 클래스로 활용하기 위해 작성함
// 즉, Writer 클래스는 게시글 작성자의 아이디와 이름값 저장
public class Writer {

	private String id; // article 테이블의 writer_id 필드(칼럼)와 연관됨
	private String name; // article 테이블의 writer_name 필드(칼럼)와 연관됨

	public Writer(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

}
