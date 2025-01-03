package org.edu.gateway.Controller;

import org.apache.dubbo.config.annotation.Reference;
import org.edu.commom.Service.UserService;
import org.edu.commom.domain.Result;
import org.edu.commom.domain.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Reference
    private UserService userService;
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public Result getList(){
        // 远程调用
        userService.saveUser(new User("1","zhangsan","123456",18));
        List<User> userList = userService.getUserList();
        return new Result.Builder<>().code(200).msg("success").data(userList).build();
    }
}
