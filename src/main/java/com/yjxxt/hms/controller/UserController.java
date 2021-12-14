package com.yjxxt.hms.controller;

import com.yjxxt.hms.base.BaseController;
import com.yjxxt.hms.base.ResultInfo;
import com.yjxxt.hms.bean.User;
import com.yjxxt.hms.model.UserModel;
import com.yjxxt.hms.query.UserQuery;
import com.yjxxt.hms.service.UserService;
import com.yjxxt.hms.utils.LoginUserUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("user")
public class UserController extends BaseController {

    //注入
    @Resource
    private UserService userService;

    //用户登录
    @RequestMapping("login")
    @ResponseBody
    public ResultInfo checkUserLogin(String userName, String userPwd){
        //创建返回值对象
        ResultInfo resultInfo = new ResultInfo();
        //调用service
        UserModel userModel = userService.checkUserLogin(userName, userPwd);
        //存储结果信息
        resultInfo.setResult(userModel);
        //返回
        return resultInfo;
    }

    //修改密码页面
    @RequestMapping("toPasswordPage")
    public String toPasswordPage(){
        return "user/password";
    }

    //修改密码
    @RequestMapping("updatePwd")
    @ResponseBody
    public ResultInfo updateUserPwd(HttpServletRequest req, String oldPwd, String newPwd, String confirmPwd){
        //实例化返回值对象
        ResultInfo resultInfo = new ResultInfo();
        //获取用户id
        int userId = LoginUserUtil.releaseUserIdFromCookie(req);
        //调用service方法
        userService.alterUserPwd(userId,oldPwd,newPwd,confirmPwd);
        return resultInfo;
    }

    //修改用户基本信息页面
    @RequestMapping("toSettingPage")
    public String toSettingPage(HttpServletRequest req){
        //获取用户信息
        int userId = LoginUserUtil.releaseUserIdFromCookie(req);
        User user = userService.selectByPrimaryKey(userId);
        //存储
        req.setAttribute("user",user);
        //跳转
        return "user/setting";
    }

    //修改用户基本信息
    @RequestMapping("setting")
    @ResponseBody
    public ResultInfo updateUserBaseInformation(User user){
        //实例化返回值对象
        ResultInfo resultInfo = new ResultInfo();
        //修改用户信息
        userService.changeUserBaseInformation(user);
        //返回结果信息
        return resultInfo;
    }

    //用户页面
    @RequestMapping("index")
    public String index(){
        return "user/user";
    }

    //分页查询
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> queryUserList(UserQuery userQuery){
        return userService.selectUserListByParams(userQuery);
    }

    //用户添加
    @RequestMapping("save")
    @ResponseBody
    public ResultInfo saveUser(User user){
        //调用service对应方法
        userService.addUser(user);
        //返回结果
        return success("添加用户成功");
    }

    //用户更新
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateUser(User user){
        //调用service对应方法
        userService.changeUser(user);
        //返回结果
        return success("更新用户成功");
    }

    //用户添加|更新 视图转发
    @RequestMapping("toAddOrUpdatePage")
    public String toAddOrUpdatePage(Integer id, Model model){
        //检验
        if (id!=null){
            //获取用户
            User user = userService.selectByPrimaryKey(id);
            //System.out.println(user);
            //存储
            model.addAttribute("user",user);
        }
        //跳转
        return "user/add_update";
    }

    //用户批量删除
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteUser(Integer[] ids){
        //调用service方法
        userService.removeUsers(ids);
        return success("删除成功");
    }


}
