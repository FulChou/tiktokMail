package org.edu.common.domain.settlement;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SettlementRequestDTO {
    private String orderId;
    private String userId;
    private List<DiscountDTO> discounts;
    private BigDecimal shippingFee; //电商商品需要运费
}
