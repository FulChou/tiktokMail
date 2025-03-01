package org.edu.common.domain.settlement;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
@Data
public class RefundRequestDTO implements Serializable {
    private Long orderId;
    private Long userId;
    private Long paymentTransactionId;
    private BigDecimal refundAmount; //退款金额
    private String refundReason;
    private BigDecimal paymentAmount;

}
