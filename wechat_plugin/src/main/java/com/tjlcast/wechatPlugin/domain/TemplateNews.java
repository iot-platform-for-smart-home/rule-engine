package com.tjlcast.wechatPlugin.domain;

import java.util.TreeMap;

/**
 *  模板消息
 */
public class TemplateNews extends BaseMessage{
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
	// 所需跳转到的小程序appid
	private String appid;
	// 所需跳转到小程序的具体页面路径，支持带参数,（示例index?foo=bar）
	private String pagepath;
	// 模板数据
	private TreeMap<String, TreeMap<String, String>> data;
	// 模板内容字体颜色，不填默认为黑色
	private String color;




	/**
	 *  为 data 中每一个key的值指定颜色
	 * @param value
	 * @param color
	 * @return  TreeMap
	 */
	public static TreeMap<String, String> item(String value, String color) {
		TreeMap<String, String> params = new TreeMap<String, String>();
		params.put("value", value);
		params.put("color", color);
		return params;
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

	public TreeMap<String, TreeMap<String, String>> getData() {
		return data;
	}

	public void setData(TreeMap<String, TreeMap<String, String>> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "TemplateNews{" +
				"touser='" + touser + '\'' +
				", template_id='" + template_id + '\'' +
				", data=" + data +
				'}';
	}
}


/**
 *   设备异常模板消息
 * {
 * 		"touser":"openid",
 * 		"template_id":"ngqIpbwh8bUfcSsECmogfXcV14J0tQlEpBO27izEYtY",
 * 		"url":"http://weixin.qq.com/download",
 * 		"miniprogram":{
 * 			"appid":"xiaochengxuappid12345",
 * 			"pagepath":"index?foo=bar"
 * 		},
 * 		"data":{
 * 			"first": {
 * 				"value":" 设备异常提醒",
 * 				"color":"#173177"
 * 			},
 * 			"keyword1":{
 * 				"value":"温度传感器",
 * 				"color":"#173177"
 * 			},
 * 			"keyword2": {
 * 				"value":"20180726",
 * 				"color":"#173177"
 * 			},
 * 			"keyword3": {
 * 				"value":"设备故障",
 * 				"color":"#173177"
 * 			},
 * 			"remark":{
 * 				"value":" 请您及时登录平台查看！",
 * 				"color":"#173177"
 * 			}
 * 		}
 * 	}
 **/