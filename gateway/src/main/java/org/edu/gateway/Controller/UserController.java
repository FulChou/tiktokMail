package org.edu.gateway.Controller;

import org.apache.dubbo.config.annotation.Reference;
import org.edu.common.service.UserServiceRPC;
import org.edu.common.domain.response.Result;
import org.edu.common.domain.UserDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    // user->role->permission
    // userid 1n roleid
    // roleid 1n permissionid

    @Reference
    private UserServiceRPC userService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public Result<List<UserDTO>> getList(){
        List<UserDTO> userList = userService.getUserList();
        // verify the result:
        if(userList == null || userList.size() == 0){
            return new Result.Builder().msg("查询失败").code(500).build();
        }
        return new Result.Builder().msg("success").data(userList).code(200).build();
    }

    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public Result register(@RequestBody UserDTO user){
        // 参数验证：
        if(user == null){
            return new Result.Builder().msg("参数错误").code(400).build();
        }
        int res = userService.saveUser(user);
        // 结果校验：
        if(res == 0){
            return new Result.Builder().msg("注册失败").code(500).build();
        }
        return new Result.Builder().msg("success").code(200).build();
    }

}
