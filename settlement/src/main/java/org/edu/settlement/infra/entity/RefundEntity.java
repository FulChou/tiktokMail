package org.edu.settlement.infra.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("refunds")
public class RefundEntity {
    @TableId(value = "id", type = IdType.AUTO) // 主键，自动生成
    private Long id;  // 退款ID（主键）

    @TableField("order_id")
    private Long orderId;  // 订单ID（外键，关联到 orders 表）

    @TableField("payment_transaction_id")
    private Long paymentTransactionId;  // 支付交易ID（外键，关联到 payment_transactions 表）

    @TableField("settlement_bill_id")
    private Long settlementBillId;  // 结算单ID（外键，关联到 settlement_bills 表）

    @TableField("refund_amount")
    private BigDecimal refundAmount;  // 退款金额

    @TableField("refund_status")
    private boolean refundStatus;  // 退款状态（false：退款失败，true：退款成功）

    @TableField("created_at")
    private Date createdAt;  // 创建时间

    @TableField("updated_at")
    private Date updatedAt;  // 更新时间
}
