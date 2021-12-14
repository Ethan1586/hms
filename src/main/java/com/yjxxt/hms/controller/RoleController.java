package com.yjxxt.hms.controller;

import com.yjxxt.hms.base.BaseController;
import com.yjxxt.hms.bean.Role;
import com.yjxxt.hms.service.RoleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("role")
public class RoleController extends BaseController {

    //注入
    @Resource
    private RoleService roleService;

    //查询所有角色
    @RequestMapping("queryAllRoles")
    @ResponseBody
    public List<Map<String,Object>> queryAllRoles(Integer userId){
        return roleService.queryAllRoles(userId);
    }
}
