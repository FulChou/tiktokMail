package org.edu.common.domain.payment;

import lombok.Data;

import java.io.Serializable;

@Data
public class PaymentResponseDTO implements Serializable {
    private Long paymentId;
    private boolean success;
    private String message;
    private String transactionId; // 第三方支付交易ID
    private Integer paymentStatus; // "SUCCESS", "FAILED", "PENDING"
}
