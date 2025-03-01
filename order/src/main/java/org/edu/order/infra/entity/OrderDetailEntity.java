package org.edu.order.infra.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("order_detail")
public class OrderDetailEntity {
    @TableId
    private Long id;  // 订单商品ID（主键）

    @TableField("order_id")
    private Long orderId;  // 订单ID（外键，关联到 orders 表）

    @TableField("product_id")
    private Long productId;  // 商品ID（外键，关联到 products 表）

    @TableField("quantity")
    private Integer quantity;  // 购买数量

    @TableField("price")
    private BigDecimal price;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;  // 创建时间

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;  // 更新时间
}
