package org.edu.user.infra.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.edu.user.infra.entity.UserEntity;

public interface UserMapper extends BaseMapper<UserEntity> {


    void saveUser(UserEntity user);
}
