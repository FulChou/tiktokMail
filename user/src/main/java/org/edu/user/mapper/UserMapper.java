package org.edu.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
//import org.edu.user.model.UserEntity; // 或者 org.edu.common.domain.User
import org.edu.commom.domain.User;
@Mapper
public interface UserMapper extends BaseMapper<User> { // 或者 User
    // 自定义查询方法可在此处定义
}