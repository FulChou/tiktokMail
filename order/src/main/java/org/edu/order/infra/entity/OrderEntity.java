package org.edu.order.infra.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.edu.common.constant.OrderStatusConstants;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("orders")
public class OrderEntity {
    @TableId
    private Long id;  // 订单ID（主键）

    @TableField("order_number")
    private String orderNumber;  // 订单编号（用户看）

    @TableField("user_id")
    private Long userId;  // 用户ID（外键）

    @TableField("total_price")
    private BigDecimal totalPrice;  // 总价

    @TableField("payment_transaction_id")
    private Long paymentTransactionId;  // 支付交易ID

    @TableField("status")
    private Integer status = OrderStatusConstants.PENDING_PAYMENT;  // 订单状态（0：待支付，1：已支付，2：已发货，3：已完成，4：已取消）

    @TableField("deleted")
    private Boolean deleted = false;  // 软删除

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;  // 创建时间

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;  // 更新时间
}
