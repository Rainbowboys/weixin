package com.weixin.entity;

import java.util.List;

public class NewsMessage extends BaseMessage {
	private int ArticleCount;// ��Ϣ����
	private List<News> Articles;// ��Ϣ��

	public int getArticleCount() {
		return ArticleCount;
	}

	public List<News> getArticles() {
		return Articles;
	}

	public void setArticles(List<News> articles) {
		Articles = articles;
	}

	public void setArticleCount(int articleCount) {
		ArticleCount = articleCount;
	}

}
