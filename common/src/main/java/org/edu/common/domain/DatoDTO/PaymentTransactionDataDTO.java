package org.edu.common.domain.DatoDTO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentTransactionDataDTO implements Serializable {
    private Long id;               // 支付交易ID

    private Long orderId;        // 关联订单ID

    private Long settlementBillId; // 关联结算单ID

    private Long userId;           // 用户ID

    private BigDecimal amount;     // 支付金额

    private String paymentMethod;  // 支付方式（如 "ALIPAY", "WECHAT", "CREDIT_CARD"）

    private Integer paymentStatus; // 支付状态

    private String transactionId;  // 第三方支付交易ID

    private String message;        // 支付失败时的消息

    private LocalDateTime createdAt; // 创建时间

    private LocalDateTime updatedAt; // 更新时间
}
