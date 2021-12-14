package com.yjxxt.hms.service;

import com.yjxxt.hms.base.BaseService;
import com.yjxxt.hms.bean.Role;
import com.yjxxt.hms.mapper.RoleMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class RoleService extends BaseService<Role,Integer> {

    //注入
    @Resource
    private RoleMapper roleMapper;

    //查询所有角色
    public List<Map<String,Object>> queryAllRoles(Integer userId){
        return roleMapper.selectAllRoles(userId);
    }
}
