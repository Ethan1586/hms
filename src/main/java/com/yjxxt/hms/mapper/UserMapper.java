package com.yjxxt.hms.mapper;

import com.yjxxt.hms.base.BaseMapper;
import com.yjxxt.hms.bean.User;
import org.apache.ibatis.annotations.MapKey;

import java.util.List;
import java.util.Map;

public interface UserMapper extends BaseMapper<User,Integer> {
    //根据名字查询用户
    public User selectUserByName(String name);

}