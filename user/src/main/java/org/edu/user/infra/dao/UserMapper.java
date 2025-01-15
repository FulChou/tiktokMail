package org.edu.user.infra.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.edu.user.infra.entity.UserEntity;

@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {

}
