package org.edu.common.domain.settlement;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DiscountDTO {

    private String discountCode; // 优惠码
    private BigDecimal discountAmount; // 优惠金额（优惠比例） 可更加特定的进行设计多个优惠方案（优惠子类）
}
