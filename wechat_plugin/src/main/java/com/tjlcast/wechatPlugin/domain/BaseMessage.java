package com.tjlcast.wechatPlugin.domain;

/**
 *  消息模型(基类）  （公众号 -> 用户）
 */
public class BaseMessage {
	// 要发送的用户账号（openId)
	private String ToUser;
	// 开发者微信号
	private String FromUserName;
	// 消息创建时间
	private long CreateTime;
	// 消息类型(text/image/location/link)
	private String MsgType;


	public String getToUser() {
		return ToUser;
	}
	public void setToUser(String toUserName) {
		ToUser = ToUser;
	}
	public String getFromUserName() {
		return FromUserName;
	}
	public void setFromUserName(String fromUserName) {
		FromUserName = fromUserName;
	}
	public long getCreateTime() {
		return CreateTime;
	}
	public void setCreateTime(long createTime) {
		CreateTime = createTime;
	}
	public String getMsgType() {
		return MsgType;
	}
	public void setMsgType(String msgType) {
		MsgType = msgType;
	}

	@Override
	public String toString() {
		return "BaseMessage{" +
				"ToUserName='" + ToUser + '\'' +
				", FromUserName='" + FromUserName + '\'' +
				", CreateTime=" + CreateTime +
				", MsgType='" + MsgType + '\'' +
				'}';
	}
}
