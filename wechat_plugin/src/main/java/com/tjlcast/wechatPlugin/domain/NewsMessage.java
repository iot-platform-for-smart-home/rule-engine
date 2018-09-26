package com.tjlcast.wechatPlugin.domain;

import java.util.List;

public class NewsMessage extends BaseMessage{
	// 图文消息个数，限制10条以内
	private int ArticleCount;
	// 多条图文消息
	private List<News> Articles;

	public int getArticleCount() {
		return ArticleCount;
	}
	public void setArticleCount(int articleCount) {
		ArticleCount = articleCount;
	}
	public List<News> getArticles() {
		return Articles;
	}
	public void setArticles(List<News> articles) {
		Articles = articles;
	}

	@Override
	public String toString() {
		return "NewsMessage{" +
				"ArticleCount=" + ArticleCount +
				", Articles=" + Articles +
				'}';
	}
}
