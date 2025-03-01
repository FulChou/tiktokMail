package org.edu.settlement.infra.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("settlement_bills")
public class SettlementEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;  // 结算单ID（主键）

    @TableField("order_id")
    private Long orderId;  // 订单ID（外键，关联到 orders 表）

    @TableField("total_amount")
    private BigDecimal totalAmount;  // 结算总金额

    @TableField("status")
    private Integer status;  // 结算状态（0：待结算，1：已结算，2：已退款）

    @TableField("payment_id")
    private Long paymentId;  // 关联的支付交易ID（外键，关联到 payment_transactions 表）

    @TableField("created_at")
    private LocalDateTime createdAt;  // 创建时间

    @TableField("updated_at")
    private LocalDateTime updatedAt;  // 更新时间
}
