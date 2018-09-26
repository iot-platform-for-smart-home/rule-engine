package com.tjlcast.wechatPlugin.controller;

import com.tjlcast.wechatPlugin.domain.wechat;
import com.tjlcast.wechatPlugin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description:Controller层：调用Service层的代码实现数据的增删查改
 */
@RestController  // RESTful 控制器类注解
@RequestMapping("api/v1/wechatplugin/")   //绑定 url 映射方法
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * @Description:查找所有用户
     * @url  访问地址：http://localhost:8080/users/findAll
     * @return 用户对象列表
     */
    @RequestMapping(value="findAll",method=RequestMethod.GET)
    public List<wechat> findAll() {
        System.out.println("查询所有");
        List<wechat> userList = userService.findAll();
        for(wechat user:userList) {
            System.out.println("用户："+user);
        }
        return userList;
    }

    /**
     * @Description:根据微信名查找用户openid
     * @url
     * @param nickname
     * @return 用户对象
     */
    @RequestMapping(value="findByNickName",method=RequestMethod.GET)
    public String findByNickName(@RequestParam("nickname") String nickname) {
        System.out.println("根据微信名查询用户");
        wechat user = userService.findByNickName(nickname);
        System.out.println("用户:"+user);
        return "{ \"openid\"=\"" + user.getOpenid()+"\"}";
    }

    /**
     * @Description:新增用户
     * @url  访问地址：
     * @param id,nickname,openid
     * @return  "success" or "false"
     */
    @RequestMapping(value="insert")
    public String insert(@RequestParam("id") String id,
                         @RequestParam("nickname")String nickname,
                         @RequestParam("openid") String openid) {
        System.out.println("新增用户");
        int result = userService.insert(id,nickname,openid);
        if(result==1) {
            System.out.println("新增成功");
            return "success";
        }else {
            System.out.println("新增失败");
            return "error";
        }
    }
    /**
     * @Description:修改用户
     * @url
     * @param id
     * @param name
     * @return
     */
//    @RequestMapping("updateOpenid")
//    public String update(@RequestParam("nickname") String nickname) {
//        System.out.println("修改用户");
//        User user = new User();
//        user.setId(id);
//        user.setName(name);
//        System.out.println(user);
//        int result = userService.update(user);
//        if(result==1) {
//            System.out.println("修改成功");
//            return "success";
//        }else {
//            System.out.println("修改失败");
//            return "error";
//        }
//    }

    /**
     * @Description:根据用户ID修改用户
     * @url
     * @param id
     * @return
     */
//    @RequestMapping("delete")
//    public String delete(@RequestParam("id") int id) {
//        System.out.println("删除用户");
//        int result = userService.delete(id);
//        if(result==1) {
//            System.out.println("删除成功");
//            return "success";
//        }else {
//            System.out.println("删除失败");
//            return "error";
//        }
//    }
}
