package org.edu.common.domain.payment;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class PaymentOrderDTO implements Serializable {
    private Long paymentId;
    private Long settlementBillId;  // 关联的结算单ID
    private Long userId;
    private BigDecimal amount;
    private String paymentMethod;  // 例如 "ALIPAY", "WECHAT", "CREDIT_CARD"  （状态在数据库中使用数字映射即可，可以在常量中定义这些状态）
    private Integer paymentStatus;  // "PENDING", "SUCCESS", "FAILED", "CANCELLED"（同上）
    //private Date createdAt;
    //private Date updatedAt;
}
