package org.edu.common.domain.settlement;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class SettlementAmountDTO implements Serializable {
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private BigDecimal shippingAmount;
}
