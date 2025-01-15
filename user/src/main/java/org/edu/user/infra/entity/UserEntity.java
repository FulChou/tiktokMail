package org.edu.user.infra.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("users")
public class UserEntity {
    @TableId(type = IdType.AUTO)
    private String id;
    private String username;
    private String password;
    private String email;
    private Date created_at;
}

