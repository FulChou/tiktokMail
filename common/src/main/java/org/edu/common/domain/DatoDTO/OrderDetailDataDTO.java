package org.edu.common.domain.DatoDTO;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderDetailDataDTO implements Serializable {
    private Long id;  // 订单商品ID

    private Long orderId;  // 订单ID

    private Long productId;  // 商品ID

    private Integer quantity;  // 购买数量

    private BigDecimal price;

    private LocalDateTime createdAt;  // 创建时间

    private LocalDateTime updatedAt;  // 更新时间
}
