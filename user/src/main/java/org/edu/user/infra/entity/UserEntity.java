package org.edu.user.infra.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;


@TableName("user")
public class UserEntity {
    @TableId
    private String id;

    private String username;

}


//pojo: 数据库
//entity: 实体
//userdomain: 用户领域
//userRepository: 用户仓库
//dto : 传输
//vo : 视图
//
//model: 业务数据

