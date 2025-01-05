package org.edu.user.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import org.apache.dubbo.config.annotation.Service;

import org.edu.commom.domain.User;
import org.edu.commom.Service.UserService;
import org.edu.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class UserServiceImp extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public void saveUser(User user) {
        // 使用 MyBatis-Plus 的 save 方法，它可以自动处理插入或更新操作
        this.save(user);
    }

    @Override
    public void updateUser(User user) {
        // 更新用户信息，根据主键 ID 更新
        this.updateById(user);
    }

    @Override
    public void deleteUser(String id) {
        // 根据 ID 删除用户
        this.removeById(id);
    }

    @Override
    public List<User> getUserList() {
        // 获取所有用户列表
        return this.list();
    }

    @Override
    public User getUserById(String id) {
        // 根据 ID 查询单个用户
        return this.getById(id);
    }

    @Override
    public List<User> getUsersByUsername(String username) {
        // 根据用户名查询用户列表
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return this.list(queryWrapper);
    }
}