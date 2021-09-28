package article.service;

import article.model.Article;

public class ArticleData {

	private Article article;

	public ArticleData(Article article) {
		this.article = article;
	}

	public Article getArticle() {
		return article;
	}

}
