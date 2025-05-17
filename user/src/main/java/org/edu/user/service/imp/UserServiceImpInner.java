package org.edu.user.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.edu.user.infra.dao.UserMapper;
import org.edu.user.infra.entity.UserEntity;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpInner extends ServiceImpl<UserMapper, UserEntity> {

}
