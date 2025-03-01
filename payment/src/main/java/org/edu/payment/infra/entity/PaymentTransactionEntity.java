package org.edu.payment.infra.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("payment_transactions")
public class PaymentTransactionEntity {
    @TableId(type = IdType.AUTO)  // 主键自动递增
    @TableField("id")             // 映射数据库中的字段
    private Long id;               // 支付交易ID（主键）

    @TableField("order_id")
    private Long orderId;        // 关联订单ID

    @TableField("settlement_bill_id")
    private Long settlementBillId; // 关联结算单ID

    @TableField("user_id")
    private Long userId;           // 用户ID

    @TableField("amount")
    private BigDecimal amount;     // 支付金额

    @TableField("payment_method")
    private String paymentMethod;  // 支付方式（如 "ALIPAY", "WECHAT", "CREDIT_CARD"）

    @TableField("payment_status")
    private Integer paymentStatus; // 支付状态（0：待支付，1：支付成功，2：支付失败，3：已取消）

    @TableField("transaction_id")
    private String transactionId;  // 第三方支付交易ID

    @TableField("message")
    private String message;        // 支付失败时的消息

    @TableField("created_at")
    private LocalDateTime createdAt; // 创建时间

    @TableField("updated_at")
    private LocalDateTime updatedAt; // 更新时间
}
