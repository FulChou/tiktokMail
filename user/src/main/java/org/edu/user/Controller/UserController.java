package org.edu.user.Controller;

import org.apache.dubbo.config.annotation.DubboService;
import org.edu.commom.domain.Result;
import org.edu.commom.domain.User;
//import org.edu.commom.Service.UserService;
import org.edu.user.service.UserServiceImp;
import org.springframework.web.bind.annotation.*;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserServiceImp userService;

    public UserController(UserServiceImp userService) {
        this.userService = userService;
    }

    // 添加新用户
    @PostMapping
    public Result createUser(@RequestBody User user) {
        userService.saveUser(user);
        return new Result.Builder<>().code(200).msg("User created").data(user).build();
    }

    // 更新现有用户信息
    @PutMapping("/{id}")
    public Result updateUser(@PathVariable String id, @RequestBody User user) {
        user.setId(id);
        userService.updateUser(user);
        return new Result.Builder<>().code(200).msg("User updated").data(user).build();
    }

    // 删除用户
    @DeleteMapping("/{id}")
    public Result deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return new Result.Builder<>().code(200).msg("User deleted").data(null).build();
    }

    // 获取所有用户列表
    @GetMapping
    public Result getAllUsers() {
        List<User> userList = userService.getUserList();
        return new Result.Builder<>().code(200).msg("success").data(userList).build();
    }

    // 根据 ID 获取单个用户
    @GetMapping("/{id}")
    public Result getUserById(@PathVariable String id) {
        User user = userService.getUserById(id);
        return new Result.Builder<>().code(200).msg("success").data(user).build();
    }
}