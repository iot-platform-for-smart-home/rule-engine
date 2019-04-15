package com.tjlcast.wechatPlugin.domain;

import com.alibaba.fastjson.JSONObject;

/**
 *  模板消息
 */
public class TemplateNews{
	/**
	 *  微信服务器交互参数
	 */
	// 要发送的用户账号 (openid)
	private String touser;
	// 模板id
	private String template_id;
	// 模板跳转链接 (非必须）
	private String url;
	// 跳小程序所需数据，不需跳小程序可不用传该数据 （非必须，若url和miniprogram都赋值了，则优先跳转小程序）
	private String miniprogram;
	// 模板数据
	private JSONObject data;

	public TemplateNews(String touser, String template_id, String url, String miniprogram, JSONObject data){
		this.touser = touser;
		this.template_id = template_id;
		this.url = url;
		this.miniprogram = miniprogram;
		this.data = data;
	}


	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public String getTemplate_id() {
		return template_id;
	}

	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}

	public JSONObject getData() {
		return data;
	}

	public void setData(JSONObject data) {
		this.data = data;
	}

	public void setMinirogram(String apppid, String pagepath){
		JSONObject miniprogram = new JSONObject();
		miniprogram.put("appid", apppid);  // 所需跳转到的小程序appid
		miniprogram.put("pagepath", pagepath);  // 所需跳转到小程序的具体页面路径，支持带参数,（示例index?foo=bar）
		this.miniprogram = miniprogram.toJSONString();
	}

	@Override
	public String toString() {
		return "TemplateNews{" +
				"touser='" + touser + '\'' +
				", template_id='" + template_id + '\'' +
				", url='" + url + '\'' +
				", miniprogram='" + miniprogram + '\'' +
				", data=" + data +
				'}';
	}
}