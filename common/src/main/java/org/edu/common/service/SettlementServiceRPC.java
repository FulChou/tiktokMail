package org.edu.common.service;

import org.edu.common.domain.settlement.*;

public interface SettlementServiceRPC {

    /**
     * 计算订单结算金额
     */
    SettlementAmountDTO calculateSettlementAmount(SettlementRequestDTO request);

    /**
     * 生成结算账单
     */
    SettlementBillDTO generateSettlementBill(SettlementRequestDTO request);

    /**
     * 更新结算状态
     */
    boolean updateSettlementStatus(String billId, String status);

    /**
     * 处理退款请求
     */
    RefundResponseDTO processRefund(RefundRequestDTO request);

    /**
     * 查询结算账单
     */
    SettlementBillDTO getSettlementBill(String billId);
}
