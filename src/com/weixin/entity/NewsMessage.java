package com.weixin.entity;

import java.util.List;

public class NewsMessage extends BaseMessage {
	private int ArticleCount;// 消息条数
	private List<News> Articles;// 消息体

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
