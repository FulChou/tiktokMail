package org.edu.common.domain.settlement;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class RefundRequestDTO {
    private String orderId;
    private String userId;
    private BigDecimal refundAmount;
    private String refundReason;
}
