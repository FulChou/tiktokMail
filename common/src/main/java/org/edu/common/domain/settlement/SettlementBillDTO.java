package org.edu.common.domain.settlement;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class SettlementBillDTO implements Serializable {
    private Long billId; // 结算单id
    private Long orderId;
    private BigDecimal settlementAmount; // 结算金额
    private BigDecimal paidAmount; // 支付金额
    private Integer paymentStatus;  // 如 "PAID", "PENDING" （在数据库中映射为数字即可）
    private LocalDateTime paymentDate;
}
