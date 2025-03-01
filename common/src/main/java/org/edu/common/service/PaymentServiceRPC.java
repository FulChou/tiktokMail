package org.edu.common.service;

import org.edu.common.domain.payment.PaymentOrderDTO;
import org.edu.common.domain.payment.PaymentRequestDTO;
import org.edu.common.domain.payment.PaymentResponseDTO;

public interface PaymentServiceRPC {
    /**
     * 创建支付订单
     * 1. 根据结算单ID，生成支付订单，并返回支付信息
     * 2. 支付订单包含支付金额、支付方式、支付状态等信息
     * 3. 订单创建后状态应为 "PENDING"
     */
    PaymentOrderDTO createPayment(PaymentRequestDTO request);

    /**
     * 执行支付
     * 1. 根据支付订单ID，执行实际支付操作
     * 2. 调用第三方支付接口（如支付宝、微信、Stripe等）
     * 3. 更新支付状态（"SUCCESS" 或 "FAILED"）
     */
    PaymentResponseDTO executePayment(Long paymentId);

    /**
     * 取消支付（用户主动取消）
     * 1. 用户可在支付未完成前主动取消支付
     * 2. 取消后，支付状态更新为 "CANCELLED"
     * 3. 若已付款，需发起退款流程
     */
    boolean cancelPayment(Long paymentId);

    /**
     * 定时取消支付（超时未支付自动取消）
     * 1. 后台定时任务检查支付订单（建议使用消息队列，发送一个延迟15分钟的消息给自己处理）
     * 2. 若支付订单超过一定时间未支付，则自动取消
     */
    void autoCancelUnpaidPayments();

    /**
     * 查询支付状态
     * 1. 查询某个支付订单的当前支付状态
     * 2. 返回支付金额、支付方式、状态等信息
     */
    PaymentOrderDTO getPaymentStatus(Long paymentId);
}
