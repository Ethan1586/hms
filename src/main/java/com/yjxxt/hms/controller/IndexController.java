package com.yjxxt.hms.controller;

import com.yjxxt.hms.base.BaseController;
import com.yjxxt.hms.bean.User;
import com.yjxxt.hms.service.UserService;
import com.yjxxt.hms.utils.LoginUserUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController extends BaseController {

    //注入
    @Resource
    private UserService userService;

    //index页面
    @RequestMapping("index")
    public String index(){
        return "index";
    }

    //welcome界面
    @RequestMapping("welcome")
    public String welcome(){
        return "welcome";
    }

    //main页面
    @RequestMapping("main")
    public String main(HttpServletRequest req){
        //获取用户id
        int userId = LoginUserUtil.releaseUserIdFromCookie(req);
        //获取用户对象
        User user = userService.selectByPrimaryKey(userId);
        //存储
        req.setAttribute("user",user);
        //返回
        return "main";
    }
}
