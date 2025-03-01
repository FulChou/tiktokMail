package org.edu.common.domain.settlement;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Data
public class RefundResponseDTO implements Serializable {
    private boolean refundStatus;
    private Long refundTransactionId;
    private Date refundDate;
}
