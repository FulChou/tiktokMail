package org.edu.common.domain.payment;

import lombok.Data;

@Data
public class PaymentResponseDTO {
    private String paymentId;
    private boolean success;
    private String message;
    private String transactionId; // 第三方支付交易ID
    private String paymentStatus; // "SUCCESS", "FAILED", "PENDING"
}
