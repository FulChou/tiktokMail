package org.edu.common.domain.DatoDTO;

import lombok.Data;
import org.edu.common.constant.OrderStatusConstants;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderDataDTO implements Serializable {
    private Long id;  // 订单ID

    private String orderNumber;  // 订单编号（用户看）

    private Long userId;  // 用户ID

    private BigDecimal totalPrice;  // 总价

    private Long paymentTransactionId;  // 支付交易ID

    private Integer status = OrderStatusConstants.PENDING_PAYMENT;  // 订单状态

    private Boolean deleted = false;  // 软删除

    private LocalDateTime createdAt;  // 创建时间

    private LocalDateTime updatedAt;  // 更新时间
}
