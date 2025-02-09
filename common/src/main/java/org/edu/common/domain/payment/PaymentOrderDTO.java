package org.edu.common.domain.payment;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PaymentOrderDTO {
    private String paymentId;
    private String settlementBillId;  // 关联的结算单ID
    private String userId;
    private BigDecimal amount;
    private String paymentMethod;  // 例如 "ALIPAY", "WECHAT", "CREDIT_CARD"  （状态在数据库中使用数字映射即可，可以在常量中定义这些状态）
    private String paymentStatus;  // "PENDING", "SUCCESS", "FAILED", "CANCELLED"（同上）
    private Date createdAt;
    private Date updatedAt;
}
