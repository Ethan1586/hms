package com.yjxxt.hms.mapper;

import com.yjxxt.hms.base.BaseMapper;
import com.yjxxt.hms.bean.Role;
import org.apache.ibatis.annotations.MapKey;

import java.util.List;
import java.util.Map;

public interface RoleMapper extends BaseMapper<Role,Integer> {
    //查询所有角色
    @MapKey("")
    public List<Map<String,Object>> selectAllRoles(Integer userId);
}