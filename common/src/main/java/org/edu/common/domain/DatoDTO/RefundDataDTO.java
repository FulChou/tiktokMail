package org.edu.common.domain.DatoDTO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class RefundDataDTO implements Serializable {
    private Long id;  // 退款ID

    private Long orderId;  // 订单ID

    private Long paymentTransactionId;  // 支付交易ID

    private Long settlementBillId;  // 结算单ID

    private BigDecimal refundAmount;  // 退款金额

    private boolean refundStatus;  // 退款状态

    private Date createdAt;  // 创建时间

    private Date updatedAt;  // 更新时间
}
