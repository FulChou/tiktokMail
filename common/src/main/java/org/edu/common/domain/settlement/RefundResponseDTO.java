package org.edu.common.domain.settlement;

import lombok.Data;

@Data
public class RefundResponseDTO {
    private boolean refundStatus;
    private String refundTransactionId;
    private String refundDate;
}
