package org.edu.common.domain.DatoDTO;

import lombok.Data;
import org.edu.common.constant.PaymentStatusConstants;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class SettlementDataDTO implements Serializable {
    private Long id;  // 结算单ID

    private Long orderId;  // 订单ID（

    private BigDecimal totalAmount;  // 结算总金额

    private Integer status = PaymentStatusConstants.PENDING;  // 结算状态

    private Long paymentId;  // 关联的支付交易ID

    private LocalDateTime createdAt;  // 创建时间

    private LocalDateTime updatedAt;  // 更新时间
}
