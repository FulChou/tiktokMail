package org.edu.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.edu.user.infra.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService extends IService<UserEntity> {

    /**
     * @param user
     * @apiNote 保存用户
     */
    void saveUser(UserEntity user);

    void updateUser(UserEntity user);

    void deleteUser(UserEntity user);

    List<UserEntity> getUserList();
}
