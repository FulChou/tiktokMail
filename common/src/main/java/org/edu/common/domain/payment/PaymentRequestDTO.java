package org.edu.common.domain.payment;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequestDTO {
    private String settlementBillId;  // 关联结算单ID
    private String userId;
    private BigDecimal amount;
    private String paymentMethod;  // 例如 "ALIPAY", "WECHAT", "CREDIT_CARD"
}
