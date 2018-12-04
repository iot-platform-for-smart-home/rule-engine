package com.tjlcast.wechatPlugin.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tjlcast.basePlugin.service.DefaultService;
import com.tjlcast.wechatPlugin.mapping.UserMapper;
import com.tjlcast.wechatPlugin.pojo.Auth;
import com.tjlcast.wechatPlugin.service.UserService;
import com.tjlcast.wechatPlugin.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl extends DefaultService implements UserService {

    @Override
    public Object service(Object[] data) {
        return null;
    }

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<String> getAllTousers(List<String> mini_openids) {
        List<String> toUsers = new ArrayList<>();
        for (String mini_openid : mini_openids){
            String toUser = getTouser(mini_openid);
            if(toUser != null && !"".equals(toUser)) {
                toUsers.add(toUser);
            }
        }
        return toUsers;
    }

    @Override
    public String getTouser(String mini_openid) {
        return userMapper.selectOaOpenidByMiniOpenid(mini_openid);
    }

    @Override
    public String judge_exist_and_follow(String unionid, String mini_openid, String oa_oepnid)throws Exception {
        Auth user = userMapper.selectByUnionid(unionid);
        JSONObject result = new JSONObject();
        if(user != null){ // 该用户存在
            if(user.getOa_openid() == null){  // 用户未注册公众号openid
                result.put("code", "unfollow");
                if(oa_oepnid != null){
                    userMapper.updateOaOpenid(unionid,oa_oepnid);
                }
            } else {
                result.put("code", "followed");
                result.put("oa_openid",user.getOa_openid());
            }
            if (user.getMini_openid() == null){ // 用户未注册小程序openid
                if(mini_openid != null) {
                    userMapper.updateMiniOpenid(unionid, mini_openid);
                }
            }
        }else{  // 用户不存在，添加进数据库
            userMapper.insert(unionid, mini_openid, oa_oepnid);
            result.put("code", "unfollow");
        }
        return result.toJSONString();
    }

    @Override
    public List<String> getAllMiniOpenids(Integer customerid, String gatewayid) {
        List<String> mini_openids = new ArrayList<>();
        List<Integer> customerids = userMapper.selectAllCustomers(customerid);
        customerids.add(customerid);
        for (Integer id : customerids){
            String mini_openid = userMapper.selectMiniOpenid(id);
            if(mini_openid != null){
                mini_openids.add(mini_openid);
            }
        }
        return mini_openids;
    }

    public void get_and_insert_users(String access_token){
        String GET_USERLIST_URL = String.format("https://api.weixin.qq.com/cgi-bin/user/get?access_token=%s&next_openid=",access_token);
        JSONObject json = CommonUtil.httpsRequest(GET_USERLIST_URL,"GET",null);
        // 解析返回结果
        int total = json.getInteger("total");
        int count = json.getInteger("count");
        JSONObject data = json.getJSONObject("data");
        JSONArray openids = data.getJSONArray("openid");
        String next_openid = json.getString("next_openid");
        // 如果(total - count)为0, 用户列表全部获取到
        while(total >= 0) {
            total -= count;
            for (int i = 0; i < count; i++) {
                // 对每一个openid，通过查找用户信息API获得对应的unionid
                String openid = openids.getString(i);
                String GET_USERINFO_URL = String.format("https://api.weixin.qq.com/cgi-bin/user/info?access_token=%s&openid=%s", access_token, openid);
                json = CommonUtil.httpsRequest(GET_USERINFO_URL, "GET", null);
                String unionid = json.getString("unionid");
                // 插入新纪录
                if (null != userMapper.selectByUnionid(unionid)) {
                    userMapper.insert(unionid, null, openid);
                }
            }
            if (total > 0) {  // 用户列表不全,获取剩余用户
                String next_url = GET_USERLIST_URL + next_openid;
                json = CommonUtil.httpsRequest(next_url,"GET",null);
                count = json.getInteger("count");
                next_openid = json.getString("next_openid");
                data = json.getJSONObject("data");
                openids = data.getJSONArray("openid");
            }
        }
    }

}

