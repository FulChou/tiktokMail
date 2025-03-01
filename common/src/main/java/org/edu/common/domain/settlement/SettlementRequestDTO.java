package org.edu.common.domain.settlement;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class SettlementRequestDTO implements Serializable {
    private Long orderId;
    private Long userId;
    //private List<DiscountDTO> discounts; //改成从nacos里直接获得
    //private BigDecimal shippingFee; //电商商品需要运费 //改成从nacos里直接获得
}
