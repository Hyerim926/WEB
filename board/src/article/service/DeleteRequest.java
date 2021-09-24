package article.service;

public class DeleteRequest {

	private String userId;
	private int articleNumber;
	
	public DeleteRequest(String userId, int articleNumber) {
		this.userId = userId;
		this.articleNumber = articleNumber;
	}

	public String getUserId() {
		return userId;
	}

	public int getArticleNumber() {
		return articleNumber;
	}
	
}
