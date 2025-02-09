package org.edu.common.domain.settlement;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class SettlementBillDTO {
    private String billId; // 结算单id
    private String orderId;
    private BigDecimal settlementAmount; // 结算金额
    private BigDecimal paidAmount; // 支付金额
    private String paymentStatus;  // 如 "PAID", "PENDING" （在数据库中映射为数字即可）
    private Date paymentDate;
}
