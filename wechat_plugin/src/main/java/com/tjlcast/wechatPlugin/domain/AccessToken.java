package com.tjlcast.wechatPlugin.domain;

/**
 *  AccesToken 模型
 */
public class AccessToken {

	private String access_token;
	private int expires_in;

	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public int getExpires_in() {
		return expires_in;
	}
	public void setExpires_in(int expires_in) {
		this.expires_in = expires_in;
	}

	/**
	 * 调试字段信息
	 * @return  Debug info
	 */
	@Override
	public String toString() {
		return "AccessToken{" +
				"access_token='" + access_token + '\'' +
				", expires_in=" + expires_in +
				'}';
	}
}
