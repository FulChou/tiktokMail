package org.edu.user.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.edu.commom.domain.User;

@Data
public class UserEntity {

    @TableId(type = IdType.AUTO)
    private String id;
    private User user; // 包含来自 common 模块的 User 对象

    // 可以根据需要添加额外的字段或方法
}