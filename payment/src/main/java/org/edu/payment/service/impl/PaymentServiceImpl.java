package org.edu.payment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.edu.common.constant.OrderStatusConstants;
import org.edu.common.constant.PaymentMethodConstants;
import org.edu.common.constant.PaymentStatusConstants;
import org.edu.common.constant.SettlementConstants;
import org.edu.common.domain.OrderDto;
import org.edu.common.domain.payment.PaymentOrderDTO;
import org.edu.common.domain.payment.PaymentRequestDTO;
import org.edu.common.domain.payment.PaymentResponseDTO;
import org.edu.common.domain.settlement.RefundRequestDTO;
import org.edu.common.domain.settlement.RefundResponseDTO;
import org.edu.common.domain.settlement.SettlementBillDTO;
import org.edu.common.service.OrderServiceRPC;
import org.edu.common.service.PaymentServiceRPC;
import org.edu.common.service.SettlementServiceRPC;
import org.edu.payment.infra.dao.PaymentTransactionMapper;
import org.edu.payment.infra.entity.PaymentTransactionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service(interfaceClass = PaymentServiceRPC.class)
public class PaymentServiceImpl implements PaymentServiceRPC {

    @Autowired
    private PaymentTransactionMapper paymentTransactionMapper;

    @Reference
    private SettlementServiceRPC settlementService;

    @Reference
    private OrderServiceRPC orderService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PaymentOrderDTO createPayment(PaymentRequestDTO request) {
        // 根据结算单ID查询结算单
        SettlementBillDTO settlementBill = settlementService.getSettlementBill(request.getSettlementBillId());

        if (settlementBill == null) {
            throw new IllegalArgumentException("未查到该ID对应的结算单: " + request.getSettlementBillId());
        }

//        //如果支付单数据库中可以找到结算单ID对应的数据，说明是重复创建，抛出异常（删去，因为一个结算单可能对应多个支付单）
//        QueryWrapper<PaymentTransactionEntity> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("settlement_bill_id", request.getSettlementBillId());
//        if (paymentTransactionMapper.selectList(queryWrapper).size() > 0) {
//            throw new IllegalArgumentException("该结算单ID对应的支付单已经存在，请勿重复创建");
//        }

        // 生成支付订单DTO
        PaymentOrderDTO paymentOrderDTO = new PaymentOrderDTO();
        paymentOrderDTO.setSettlementBillId(request.getSettlementBillId());
        paymentOrderDTO.setUserId(request.getUserId());
        paymentOrderDTO.setAmount(request.getAmount());
        paymentOrderDTO.setPaymentMethod(request.getPaymentMethod());
        paymentOrderDTO.setPaymentStatus(PaymentStatusConstants.PENDING); // 设置状态为 "PENDING"（待支付）

        // 保存支付订单到数据库
        PaymentTransactionEntity paymentTransaction = new PaymentTransactionEntity();
        // 生成支付单ID，基于当前时间戳
        paymentTransaction.setId(System.currentTimeMillis());
        paymentTransaction.setSettlementBillId(request.getSettlementBillId());
        // 设置orderId
        paymentTransaction.setOrderId(settlementBill.getOrderId());
        paymentTransaction.setUserId(request.getUserId());
        paymentTransaction.setAmount(request.getAmount());
        paymentTransaction.setPaymentMethod(request.getPaymentMethod());
        paymentTransaction.setPaymentStatus(PaymentStatusConstants.PENDING); // 设置状态为 "PENDING"（待支付）
        paymentTransaction.setTransactionId(null); // 初始时交易ID为空
        paymentTransaction.setMessage(null); // 初始时支付失败消息为空
        paymentTransaction.setCreatedAt(LocalDateTime.now());
        paymentTransaction.setUpdatedAt(LocalDateTime.now());

        paymentTransactionMapper.insert(paymentTransaction);

        //更新结算单对应的支付订单ID
        boolean success = settlementService.updatePaymentId(request.getSettlementBillId(), paymentTransaction.getId());
        if (!success) {
            throw new IllegalArgumentException("更新结算单对应的支付订单ID失败");
        }

        // 更新order对应的transactionId
//        OrderEntity order = orderMapper.selectById(paymentTransaction.getOrderId());
//        order.setPaymentTransactionId(paymentTransaction.getId());
//        orderMapper.updateById(order);
        boolean success2 =  orderService.updatePaymentTransactionId(paymentTransaction.getOrderId(), paymentTransaction.getId());
        if (!success2) {
            throw new IllegalArgumentException("更新order对应的transactionId失败");
        }

        // 返回支付订单信息
        return paymentOrderDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PaymentResponseDTO executePayment(Long paymentId) {
        // 根据支付订单ID查询支付订单
        PaymentTransactionEntity paymentTransaction = paymentTransactionMapper.selectById(paymentId);
        if (paymentTransaction == null) {
            throw new RuntimeException("未查到该ID对应的交易单: " + paymentId);
        }

        // 判断支付状态是否为待支付
        if (paymentTransaction.getPaymentStatus() != PaymentStatusConstants.PENDING) {
            throw new RuntimeException("支付状态不是待支付");
        }

        // 根据支付方式分类处理支付模拟
        String payMethod = paymentTransaction.getPaymentMethod();
        boolean isPaymentSuccessful;
        String transactionId;

        // 模拟不同支付方式的处理逻辑
        switch (payMethod.toUpperCase()) {
            case PaymentMethodConstants.ALIPAY:
                isPaymentSuccessful = simulateAlipayPayment();
                transactionId = "ALIPAY-" + UUID.randomUUID().toString();
                break;
            case PaymentMethodConstants.WECHATPAY:
                isPaymentSuccessful = simulateWechatPayment();
                transactionId = "WECHAT-" + UUID.randomUUID().toString();
                break;
            case PaymentMethodConstants.CREDIT_CARD:
                isPaymentSuccessful = simulateCreditCardPayment();
                transactionId = "CREDIT-" + UUID.randomUUID().toString();
                break;
            default:
                // 默认处理，模拟成功并生成通用的交易ID
                isPaymentSuccessful = true;
                transactionId = UUID.randomUUID().toString();
                break;
        }

        paymentTransaction.setTransactionId(transactionId);

        PaymentResponseDTO response = new PaymentResponseDTO();
        response.setPaymentId(paymentId);
        response.setPaymentId(paymentTransaction.getId());
        response.setTransactionId(transactionId);

        if (isPaymentSuccessful) {
            // 更新支付状态为成功
            paymentTransaction.setPaymentStatus(PaymentStatusConstants.SUCCESS);
            paymentTransactionMapper.updateById(paymentTransaction);

            // 更新对应orders表中的支付状态
//            OrderEntity orderEntity = orderMapper.selectById(paymentTransaction.getOrderId());
//            orderEntity.setStatus(OrderStatusConstants.PAID);
            boolean success2 = orderService.updateStatus(paymentTransaction.getOrderId(), OrderStatusConstants.PAID);
            if (!success2) {
                throw new IllegalArgumentException("更新order对应的状态失败");
            }

            response.setSuccess(true);
            response.setMessage(""); //message只存放失败信息
            response.setPaymentStatus(PaymentStatusConstants.SUCCESS);
        } else {
            // 更新支付状态为失败
            paymentTransaction.setPaymentStatus(PaymentStatusConstants.FAILED);
            paymentTransactionMapper.updateById(paymentTransaction);

            response.setSuccess(false);
            response.setMessage("Payment failed.");
            response.setPaymentStatus(PaymentStatusConstants.FAILED);  // "FAILED"
        }

        // 返回支付响应
        return response;
    }

    // 模拟支付宝支付逻辑
    private boolean simulateAlipayPayment() {
        // 根据支付宝的业务逻辑进行模拟
        return true; // 这里简单返回成功
    }

    // 模拟微信支付逻辑
    private boolean simulateWechatPayment() {
        // 根据微信支付的业务逻辑进行模拟
        return true; // 这里简单返回成功
    }

    // 模拟信用卡支付逻辑
    private boolean simulateCreditCardPayment() {
        // 根据信用卡支付的业务逻辑进行模拟
        // 增加一些随机性，有时返回失败
        if (Math.random() < 0.05) {
            return false; // 模拟部分失败的情况
        }
        return true; // 这里简单返回成功
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelPayment(Long paymentId) {
        // 查询支付交易记录
        PaymentTransactionEntity paymentTransaction = paymentTransactionMapper.selectById(paymentId);
        if (paymentTransaction == null) {
            throw new IllegalArgumentException("未查到该支付订单: " + paymentId);
        }

        // 如果支付状态为 "PENDING"（待支付），直接取消
        if (paymentTransaction.getPaymentStatus() == PaymentStatusConstants.PENDING) {
            paymentTransaction.setPaymentStatus(PaymentStatusConstants.CANCELLED);
            paymentTransactionMapper.updateById(paymentTransaction);

            //更新订单状态为已取消、调用订单服务取消订单
            OrderDto orderDTO = new OrderDto();
            orderDTO.setOrderId(paymentTransaction.getId());
            orderDTO.setStatus(OrderStatusConstants.CANCELLED);
            orderService.updateOrder(orderDTO);
            orderService.cancel(orderDTO);

            //更新结算单状态为已取消
            settlementService.updateSettlementStatus(paymentTransaction.getId(), SettlementConstants.CANCELLED);

            return true;
        }

        // 如果支付状态为 "SUCCESS"（已付款），则发起退款流程
        if (paymentTransaction.getPaymentStatus() == PaymentStatusConstants.SUCCESS) {
            // 构造退款请求
            RefundRequestDTO refundRequest = new RefundRequestDTO();
            refundRequest.setOrderId(paymentTransaction.getOrderId());
            refundRequest.setUserId(paymentTransaction.getUserId());
            refundRequest.setRefundAmount(paymentTransaction.getAmount());
            refundRequest.setRefundReason("User cancelled payment");
            refundRequest.setPaymentTransactionId(paymentTransaction.getId());
            refundRequest.setPaymentAmount(paymentTransaction.getAmount());

            // 发起退款操作
            RefundResponseDTO refundResponse = settlementService.processRefund(refundRequest);
            if (refundResponse.isRefundStatus()) {
                // 如果退款成功，更新支付状态为 "CANCELLED"
                paymentTransaction.setPaymentStatus(PaymentStatusConstants.CANCELLED);
                paymentTransactionMapper.updateById(paymentTransaction);
                return true;
            } else {
                // 如果退款失败，返回取消失败
                return false;
            }
        }

        // 如果支付状态是 "FAILED" 或已取消，返回 false
        return false;
    }

    @Override
    @Scheduled(cron = "0 */1 * * * ?")
    public void autoCancelUnpaidPayments() {
        // 定义超时时间，例如15分钟
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(15);

        // 查询支付状态为 PENDING (0) 且创建时间早于 threshold 的支付订单
        QueryWrapper<PaymentTransactionEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("payment_status", PaymentStatusConstants.PENDING)
                .lt("created_at", threshold);

        List<PaymentTransactionEntity> pendingPayments = paymentTransactionMapper.selectList(queryWrapper);

        for (PaymentTransactionEntity payment : pendingPayments) {
            // 直接更新状态
            payment.setPaymentStatus(PaymentStatusConstants.CANCELLED);
            paymentTransactionMapper.updateById(payment);

            //更新订单状态为已取消、调用订单服务取消订单
            OrderDto orderDTO = new OrderDto();
            orderDTO.setOrderId(payment.getId());
            orderDTO.setStatus(OrderStatusConstants.CANCELLED);
            orderService.updateOrder(orderDTO);
            orderService.cancel(orderDTO);

            //更新结算单状态为已取消
            settlementService.updateSettlementStatus(payment.getId(), SettlementConstants.CANCELLED);
        }
    }

    @Override
    public PaymentOrderDTO getPaymentStatus(Long paymentId) {
        // 根据支付订单ID查询支付交易记录
        PaymentTransactionEntity paymentTransaction = paymentTransactionMapper.selectById(paymentId);
        if (paymentTransaction == null) {
            throw new IllegalArgumentException("支付订单未找到，ID: " + paymentId);
        }

        // 将查询结果转换为 PaymentOrderDTO
        PaymentOrderDTO paymentOrderDTO = new PaymentOrderDTO();
        paymentOrderDTO.setPaymentId(paymentTransaction.getId());
        paymentOrderDTO.setSettlementBillId(paymentTransaction.getSettlementBillId());
        paymentOrderDTO.setUserId(paymentTransaction.getUserId());
        paymentOrderDTO.setAmount(paymentTransaction.getAmount());
        paymentOrderDTO.setPaymentMethod(paymentTransaction.getPaymentMethod());
        paymentOrderDTO.setPaymentStatus(paymentTransaction.getPaymentStatus());

        return paymentOrderDTO;
    }
}
