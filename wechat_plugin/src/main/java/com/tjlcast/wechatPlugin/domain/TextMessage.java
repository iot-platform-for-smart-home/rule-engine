package com.tjlcast.wechatPlugin.domain;

/**
 * 	文本消息
 */
public class TextMessage extends BaseMessage{
	// 发送给用户的消息内容
	private String Content;

	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}

	@Override
	public String toString() {
		return "TextMessage{" +
				"Content='" + Content + '\'' + '}';
	}
}
