package org.edu.common.domain.payment;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class PaymentRequestDTO implements Serializable {
    private Long settlementBillId;  // 关联结算单ID
    private Long userId;
    private BigDecimal amount;
    private String paymentMethod;  // 例如 "ALIPAY", "WECHAT", "CREDIT_CARD"
}
