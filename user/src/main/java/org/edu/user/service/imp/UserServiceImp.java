package org.edu.user.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.edu.user.infra.dao.UserMapper;
import org.edu.user.infra.entity.UserEntity;
import org.edu.user.service.UserService;

import java.util.ArrayList;
import java.util.List;



public class UserServiceImp extends ServiceImpl<UserMapper, UserEntity> implements UserService {


    @Override
    public void saveUser(UserEntity user) {

        // userMapper.insert(user);
    }

    @Override
    public void updateUser(UserEntity user) {

    }

    @Override
    public void deleteUser(UserEntity user) {

        this.deleteUser(user);
    }

    @Override
    public List<UserEntity> getUserList() {
        List<UserEntity> userDTOList = new ArrayList<>();
        for (int i = 0; i < 3; ++i) {
            UserEntity userDTO = new UserEntity();
            userDTOList.add(userDTO);
        }

        return userDTOList;
    }
}
