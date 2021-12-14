package com.yjxxt.hms.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjxxt.hms.base.BaseService;
import com.yjxxt.hms.bean.Role;
import com.yjxxt.hms.bean.User;
import com.yjxxt.hms.bean.UserRole;
import com.yjxxt.hms.mapper.UserMapper;
import com.yjxxt.hms.mapper.UserRoleMapper;
import com.yjxxt.hms.model.UserModel;
import com.yjxxt.hms.query.UserQuery;
import com.yjxxt.hms.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class UserService extends BaseService<User,Integer> {

    //注入
    @Resource
    private UserMapper userMapper;

    //注入
    @Resource
    private UserRoleMapper userRoleMapper;

    //验证用户登录
    public UserModel checkUserLogin(String userName,String userPwd){
        //验证参数
        checkUserParams(userName,userPwd);
        //验证用户是否存在
        User temp = userMapper.selectUserByName(userName);
        AssertUtil.isTrue(null==temp,"用户不存在或已注销");
        //验证密码
        checkUserPwd(userPwd,temp.getUserPwd());
        //实例化返回值对象
        UserModel userModel = new UserModel();
        //设置用户信息
        userModel.setUserName(temp.getUserName());
        userModel.setTrueName(temp.getTrueName());
        //加密用户id
        userModel.setUserIdStr(UserIDBase64.encoderUserID(temp.getId()));
        //返回用户信息
        return userModel;
    }

    //验证用户密码是否正确
    private void checkUserPwd(String userPwd, String pwd) {
        //数据库密码已加密，先加密密码
        userPwd = Md5Util.encode(userPwd);
        //密码与数据库一致
        AssertUtil.isTrue(!(userPwd.equals(pwd)),"用户密码错误");
    }

    //验证用户登录参数
    private void checkUserParams(String userName, String userPwd) {
        //用户名不能为空
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空");
        //用户密码不能为空
        AssertUtil.isTrue(StringUtils.isBlank(userPwd),"用户密码不能为空");
    }

    //修改密码验证
    @Transactional(propagation = Propagation.REQUIRED)
    public void alterUserPwd(Integer userId,String oldPwd,String newPwd,String confirmPwd){
        //获取User对象
        User user = userMapper.selectByPrimaryKey(userId);
        //修改密码参数验证
        checkAlterUserPwd(user,oldPwd,newPwd,confirmPwd);
        //设置新密码 加密
        user.setUserPwd(Md5Util.encode(newPwd));
        //验证修改密码是否成功
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user)<1,"用户密码修改失败");
    }

    //修改密码参数验证
    private void checkAlterUserPwd(User user, String oldPwd, String newPwd, String confirmPwd) {
        //用户存在
        AssertUtil.isTrue(null==user,"用户未登录或不存在");
        //原密码不能为空
        AssertUtil.isTrue(StringUtils.isBlank(oldPwd),"请填写原始密码");
        //原密码要与数据库密码保持一致
        AssertUtil.isTrue(!(user.getUserPwd().equals(Md5Util.encode(oldPwd))),"请输入正确的原始密码");
        //新密码不能为空
        AssertUtil.isTrue(StringUtils.isBlank(newPwd),"请填写新密码");
        //新密码不能与原始密码一致
        AssertUtil.isTrue(oldPwd.equals(newPwd),"新密码与原始密码相同，请重新输入");
        //确认密码不能为空
        AssertUtil.isTrue(StringUtils.isBlank(confirmPwd),"请填写确认密码");
        //确认密码与新密码相同
        AssertUtil.isTrue(!(newPwd.equals(confirmPwd)),"确认密码应与新密码密码相同，请重新输入");
    }

    //修改用户基本信息
    public void changeUserBaseInformation(User user){
        //验证用户是否存在
        User temp = userMapper.selectByPrimaryKey(user.getId());
        AssertUtil.isTrue(null==temp,"用户不存在或未登录");
        //验证用户基本信息参数
        checkUserBaseInformation(user.getUserName(),user.getEmail(),user.getPhone());
        //用户名已存在问题解决
        User temp2 = userMapper.selectUserByName(user.getUserName());
        AssertUtil.isTrue(null!=temp2 && !(temp2.getId().equals(user.getId())),"用户名已存在");
        //设置默认值
        user.setUpdateDate(new Date());
        //检验是否修改成功
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user)<1,"用户信息更改失败");
    }

    //用户基本信息参数验证
    private void checkUserBaseInformation(String userName, String email, String phone) {
        //用户米不能为空
        AssertUtil.isTrue(null==userName,"请填写用户名");
        //email不能为空
        AssertUtil.isTrue(null==email,"请填写邮箱");
        //phone不能为空
        AssertUtil.isTrue(null==phone,"请填写联系电话");
        //phone合法
        AssertUtil.isTrue(!(PhoneUtil.isMobile(phone)),"请填写合法的联系电话");
    }

    //用户列表分页查询
    public Map<String,Object> selectUserListByParams(UserQuery userQuery){
        //实例化返回值对象
        Map<String,Object> map = new HashMap<String,Object>();
        //开始分页
        PageHelper.startPage(userQuery.getPage(),userQuery.getLimit());
        PageInfo<User> pageInfo = new PageInfo<User>(selectByParams(userQuery));
        //设置默认值
        map.put("code",0);
        map.put("msg",0);
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        //返回
        return map;
    }

    //用户添加
    @Transactional(propagation = Propagation.REQUIRED)
    public void addUser(User user){
        //验证用户参数
        checkUserBaseInformation(user.getUserName(),user.getEmail(),user.getPhone());
        //设定默认值
        user.setIsValid(1);
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        //设定加密密码
        user.setUserPwd(Md5Util.encode("123"));

        //验证结果
        AssertUtil.isTrue(insertSelective(user)<1,"用户记录添加失败");
        //与角色关联
        relationUserRole(user.getId(),user.getRoleIds());
    }

    //用户角色关联
    private void relationUserRole(Integer userId, String roleIds) {
        //如果原始角色存在 删除 增加新角色
        //查询用户所拥有角色数量
        int count = userRoleMapper.selectRolesCountsByUserId(userId);
        if (count>0){
            //删除原有角色
            AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(userId)!=count,"分配用户角色失败");
        }
        //重新添加角色
        if (StringUtils.isNotBlank(roleIds)){
            List<UserRole> uRList = new ArrayList<UserRole>();
            for (String s:roleIds.split(",")) {
                //实例化用户角色对象
                UserRole userRole = new UserRole();
                //设置值
                userRole.setRoleId(Integer.parseInt(s));
                userRole.setUserId(userId);
                userRole.setCreateDate(new Date());
                userRole.setUpdateDate(new Date());
                uRList.add(userRole);
            }
            //检验
            AssertUtil.isTrue(userRoleMapper.insertBatch(uRList)<uRList.size(),"用户角色分配失败");
        }
    }

    //用户更新
    @Transactional(propagation = Propagation.REQUIRED)
    public void changeUser(User user){
        //验证用户存在
        User temp = selectByPrimaryKey(user.getId());
        AssertUtil.isTrue(null==temp,"用户不存在");
        //验证参数
        checkUserBaseInformation(user.getUserName(),user.getEmail(),user.getPhone());
        //设定默认值
        user.setUpdateDate(new Date());
        //验证结果
        AssertUtil.isTrue(updateByPrimaryKeySelective(user)<1,"用户记录修改失败");
        //获取用户id
        Integer userId = userMapper.selectUserByName(user.getUserName()).getId();
        relationUserRole(userId,user.getRoleIds());
    }

    //用户批量删除
    @Transactional(propagation = Propagation.REQUIRED)
    public void removeUsers(Integer[] ids){
        //待删除的记录不为空
        AssertUtil.isTrue(0==ids.length || null==ids,"请选择要删除的数据");
        //校验
        AssertUtil.isTrue(deleteBatch(ids)!=ids.length,"用户记录删除失败");
    }

    //用户删除
}
