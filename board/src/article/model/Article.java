package article.model;

import java.util.Date;

public class Article {

	private Integer number; // article 테이블의 article_no 필드(칼럼)와 연관됨
	// 작성자 정보를 보관하기 위해
	// 앞서 구현한 Writer 타입의 필드를 정의했음. 
	private Writer writer;
	private String title; // article 테이블의 title 필드(칼럼)와 연관됨
	private Date regDate; // article 테이블의 regdate 필드(칼럼)와 연관됨
	private Date modifiedDate; // article 테이블의 moddate 필드(칼럼)와 연관됨
	private int readCount; // article 테이블의 read_cnt 필드(칼럼)와 연관됨
	
	public Article(Integer number, Writer writer, String title,
			Date regDate, Date modifiedDate, int readCount) {
		this.number = number;  
		this.writer = writer;
		this.title = title;
		this.regDate = regDate;
		this.modifiedDate = modifiedDate;
		this.readCount = readCount;
	}

	public Integer getNumber() {
		return number;
	}

	public Writer getWriter() {
		return writer;
	}

	public String getTitle() {
		return title;
	}

	public Date getRegDate() {
		return regDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public int getReadCount() {
		return readCount;
	}
	
}
