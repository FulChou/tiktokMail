package org.edu.user.service;

import org.apache.dubbo.config.annotation.Service;
import org.edu.commom.Service.UserService;
import org.edu.commom.domain.User;

import java.util.ArrayList;
import java.util.List;


@Service
public class UserServiceImp implements UserService {

    @Override
    public void saveUser(User user) {
        System.out.println("注册用户");
    }

    @Override
    public void updateUser(User user) {

    }

    @Override
    public void deleteUser(User user) {

    }

    @Override
    public List<User> getUserList() {
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < 3; ++i) {
            User user = new User();
            user.setId(String.valueOf(i));
            userList.add(user);
        }

        return userList;
    }
}
