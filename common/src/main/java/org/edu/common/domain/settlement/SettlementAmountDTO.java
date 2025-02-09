package org.edu.common.domain.settlement;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SettlementAmountDTO {
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private BigDecimal shippingAmount;
}
