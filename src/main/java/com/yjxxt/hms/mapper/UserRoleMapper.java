package com.yjxxt.hms.mapper;

import com.yjxxt.hms.base.BaseMapper;
import com.yjxxt.hms.bean.UserRole;

public interface UserRoleMapper extends BaseMapper<UserRole,Integer> {

    //根据用户id查询用户持有角色
    public Integer selectRolesCountsByUserId(Integer userId);

    //根据用户id删除用户持有角色
    public Integer deleteUserRoleByUserId(Integer userId);

}