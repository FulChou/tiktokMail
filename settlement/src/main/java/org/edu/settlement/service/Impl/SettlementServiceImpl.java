package org.edu.settlement.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.dubbo.config.annotation.Reference;
import org.edu.common.domain.DatoDTO.OrderDataDTO;
import org.edu.common.domain.DatoDTO.OrderDetailDataDTO;
import org.edu.common.domain.DatoDTO.SettlementDataDTO;
import org.edu.common.domain.OrderDto;
import org.edu.common.service.OrderServiceRPC;
import org.edu.settlement.config.SettlementConfiguration;
import org.edu.common.constant.*;
import org.edu.common.domain.payment.PaymentOrderDTO;
import org.edu.common.domain.payment.PaymentRequestDTO;
import org.edu.common.domain.settlement.*;
import org.edu.common.service.PaymentServiceRPC;
import org.edu.common.service.SettlementServiceRPC;
import org.apache.dubbo.config.annotation.Service;
import org.edu.settlement.infra.dao.RefundMapper;
import org.edu.settlement.infra.dao.SettlementMapper;
import org.edu.settlement.infra.entity.RefundEntity;
import org.edu.settlement.infra.entity.SettlementEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SettlementServiceRPC.class)
public class SettlementServiceImpl implements SettlementServiceRPC {

    @Autowired
    private SettlementMapper settlementMapper;

    @Autowired
    private RefundMapper refundMapper;

    @Reference(lazy = true)
    private PaymentServiceRPC paymentService;

    @Reference
    private OrderServiceRPC orderService;

    @Autowired
    private SettlementConfiguration settlementConfiguration;

    @Override
    public SettlementDataDTO getById(Long id) {
        SettlementEntity settlementEntity = settlementMapper.selectById(id);

        SettlementDataDTO settlementDataDTO = new SettlementDataDTO();
        settlementDataDTO.setId(settlementEntity.getId());
        settlementDataDTO.setOrderId(settlementEntity.getOrderId());
        settlementDataDTO.setTotalAmount(settlementEntity.getTotalAmount());
        settlementDataDTO.setStatus(settlementEntity.getStatus());
        settlementDataDTO.setPaymentId(settlementEntity.getPaymentId());
        settlementDataDTO.setCreatedAt(settlementEntity.getCreatedAt());
        settlementDataDTO.setUpdatedAt(settlementEntity.getUpdatedAt());

        return settlementDataDTO;
    }

    @Override
    public boolean updatePaymentId(Long billId, Long paymentTransactionId) {
        SettlementEntity settlementEntity = new SettlementEntity();
        settlementEntity.setId(billId);
        settlementEntity.setPaymentId(paymentTransactionId);
        return settlementMapper.updateById(settlementEntity) > 0;
    }

    @Override
    public boolean updateStatus(Long billId, Integer status) {
        SettlementEntity settlementEntity = new SettlementEntity();
        settlementEntity.setId(billId);
        settlementEntity.setStatus(status);
        return settlementMapper.updateById(settlementEntity) > 0;
    }

    @Override
    public boolean updateSettlementStatus(Long billId, Integer status) {
        // 构造需要更新的实体对象，仅设置主键和需要更新的字段
        SettlementEntity settlementBill = new SettlementEntity();
        settlementBill.setId(billId);
        settlementBill.setStatus(status);

        // 执行更新操作
        int rows = settlementMapper.updateById(settlementBill);
        return rows > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SettlementAmountDTO calculateSettlementAmount(SettlementRequestDTO request) {
        // 从订单服务获取订单信息
        OrderDataDTO order = orderService.getById(request.getOrderId());
        if (order == null) {
            throw new IllegalArgumentException("未查询到该ID的订单：" + request.getOrderId());
        }

        BigDecimal totalPrice = order.getTotalPrice();  // 获取订单的总价

        // 从订单详情服务获取订单商品详情
        List<OrderDetailDataDTO> orderDetails = orderService.getOrderDetailsByOrderId(request.getOrderId());

        if (orderDetails == null || orderDetails.isEmpty()) {
            throw new IllegalArgumentException("未查询到该ID的订单详情：" + request.getOrderId());
        }

        // 计算商品的折扣
        BigDecimal discountAmount = calculateDiscountAmount(orderDetails);

        // 获取运费金额，从 Nacos 配置中读取
        BigDecimal shippingAmount = settlementConfiguration.getShippingFee();

        // 计算结算金额：订单总价减去折扣金额再加上运费
        BigDecimal totalAmount = totalPrice.subtract(discountAmount).add(shippingAmount);

        // 构造并返回 SettlementAmountDTO
        SettlementAmountDTO settlementAmountDTO = new SettlementAmountDTO();
        settlementAmountDTO.setTotalAmount(totalAmount);
        settlementAmountDTO.setDiscountAmount(discountAmount);
        settlementAmountDTO.setShippingAmount(shippingAmount);

        return settlementAmountDTO;
    }

    private BigDecimal calculateDiscountAmount(List<OrderDetailDataDTO> orderDetails) {
        BigDecimal discountAmount = BigDecimal.ZERO;
        // 从 Nacos 配置中获取折扣规则，Map 的 key 为商品ID，value 为折扣率
        Map<Long, Double> discountMap = settlementConfiguration.getDiscountConfig().getDiscounts();

        for (OrderDetailDataDTO detail : orderDetails) {
            BigDecimal price = detail.getPrice();
            int quantity = detail.getQuantity();
            BigDecimal totalItemPrice = price.multiply(BigDecimal.valueOf(quantity));
            Long productId = detail.getProductId();

            // 如果该商品在折扣配置中，则计算折扣后的价格
            if (discountMap.containsKey(productId)) {
                double discountRate = discountMap.get(productId);
                // 计算单个商品打折后的单价
                BigDecimal discountedUnitPrice = price.multiply(BigDecimal.valueOf(discountRate));
                BigDecimal discountedTotalPrice = discountedUnitPrice.multiply(BigDecimal.valueOf(quantity));
                // 该商品的折扣金额 = 原价 - 折扣后价格
                BigDecimal itemDiscount = totalItemPrice.subtract(discountedTotalPrice);
                discountAmount = discountAmount.add(itemDiscount);
            }
        }

        return discountAmount;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SettlementBillDTO generateSettlementBill(SettlementRequestDTO request) {
        // 根据订单ID查询订单
        OrderDataDTO order = orderService.getById(request.getOrderId());
        if (order == null) {
            throw new IllegalArgumentException("未查询到该ID的订单: " + request.getOrderId());
        }

        // 查询订单详情（每个商品的价格和数量）
        List<OrderDetailDataDTO> orderDetails = orderService.getOrderDetailsByOrderId(request.getOrderId());

        // 计算折扣金额（基于每个订单项）
        BigDecimal discountAmount = calculateDiscountAmount(orderDetails);

        // 获取运费金额，从 Nacos 配置中读取
        BigDecimal shippingAmount = settlementConfiguration.getShippingFee();

        // 计算结算金额：订单总价 - 折扣 + 运费
        BigDecimal settlementAmount = order.getTotalPrice().subtract(discountAmount).add(shippingAmount);

        // 构造结算单 DTO
        SettlementBillDTO billDTO = new SettlementBillDTO();
        billDTO.setOrderId(order.getId());
        billDTO.setSettlementAmount(settlementAmount);

        // 直接创建 SettlementEntity，并设置值
        SettlementEntity billEntity = new SettlementEntity();
        billEntity.setOrderId(billDTO.getOrderId());
        billEntity.setTotalAmount(billDTO.getSettlementAmount());
        billEntity.setStatus(SettlementConstants.PENDING); //结算单状态设置为“待支付”

        // 持久化结算单到数据库（插入 Entity）
        settlementMapper.insert(billEntity);

        //查找与订单相关联的结算单ID
        SettlementEntity settlementBill = settlementMapper.selectOne(
                new QueryWrapper<SettlementEntity>().eq("order_id", order.getId())
        );

        if (settlementBill == null) {
            throw new IllegalArgumentException("未查询到与订单ID关联的结算单");
        }
        // 获取结算单ID
        Long settlementId = settlementBill.getId();

        //创建支付订单并获取支付金额和支付状态
        PaymentRequestDTO paymentRequestDTO = new PaymentRequestDTO();
        paymentRequestDTO.setSettlementBillId(settlementId); // 关联结算单
        paymentRequestDTO.setUserId(order.getUserId()); // 用户ID
        paymentRequestDTO.setAmount(settlementAmount); // 支付金额
        //没有传支付方法，因此createPayment使用默认支付方式

        //调用 createPayment 创建支付订单，在该函数中，为结算单中的支付ID赋值
        PaymentOrderDTO paymentOrderDTO = paymentService.createPayment(paymentRequestDTO);

        // 设置结算单ID、支付金额和支付状态到结算单DTO
        billDTO.setBillId(settlementId);
        billDTO.setPaidAmount(paymentOrderDTO.getAmount()); // 设置支付金额
        billDTO.setPaymentStatus(paymentOrderDTO.getPaymentStatus()); // 设置支付状态

        return billDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RefundResponseDTO processRefund(RefundRequestDTO request) {
        // 验证订单和用户
        OrderDataDTO order = orderService.getById(request.getOrderId());
        if (order == null) {
            throw new IllegalArgumentException("未查询到该ID的订单: " + request.getOrderId());
        }

        // 确保用户是订单的拥有者
        if (!order.getUserId().equals(request.getUserId())) {
            throw new IllegalArgumentException("用户ID不匹配");
        }

        // 获取实际支付金额
        BigDecimal orderPaidAmount = request.getPaymentAmount();

        // 检查退款金额是否合法
        if (request.getRefundAmount().compareTo(orderPaidAmount) > 0) {
            throw new IllegalArgumentException("退款金额不能超过支付金额");
        }

        //更新结算记录的退款状态
        SettlementEntity settlementBill = settlementMapper.selectOne(new QueryWrapper<SettlementEntity>()
                .eq("order_id", request.getOrderId()));
        if (settlementBill != null) {
            settlementBill.setStatus(SettlementConstants.CANCELLED);
            settlementMapper.updateById(settlementBill);
        }

        //更新支付记录的退款状态在paymentService中的cancelPayment中实现

        // 保存退款记录到数据库
        RefundEntity refundEntity = new RefundEntity();
        refundEntity.setOrderId(request.getOrderId());
        refundEntity.setSettlementBillId(settlementBill.getId());
        refundEntity.setPaymentTransactionId(request.getPaymentTransactionId());
        refundEntity.setRefundAmount(request.getRefundAmount());
        refundEntity.setRefundStatus(true);
        refundMapper.insert(refundEntity);

        //更新订单状态为已取消、调用订单服务取消订单
        OrderDto orderDTO = new OrderDto();
        orderDTO.setOrderId(order.getId());
        orderDTO.setStatus(OrderStatusConstants.CANCELLED);
        orderService.updateOrder(orderDTO);
        orderService.cancel(orderDTO);

        // 获取插入后的退款记录信息
        RefundEntity insertedRefund = refundMapper.selectById(refundEntity.getId());  // 根据 ID 获取插入的记录

        // 返回退款响应
        RefundResponseDTO response = new RefundResponseDTO();
        response.setRefundStatus(true);
        response.setRefundTransactionId(insertedRefund.getId());  // 从插入的实体获取 refundTransactionId
        response.setRefundDate(insertedRefund.getCreatedAt());  // 从插入的实体获取退款日期

        return response;
    }

    @Override
    public SettlementBillDTO getSettlementBill(Long billId) {
        // 查询结算单实体
        SettlementEntity settlementBillEntity = settlementMapper.selectById(billId);
        if (settlementBillEntity == null) {
            throw new IllegalArgumentException("未查询到该ID的结算单: " + billId);
        }

        // 将查询到的实体转换为 DTO
        SettlementBillDTO settlementBillDTO = new SettlementBillDTO();
        settlementBillDTO.setBillId(settlementBillEntity.getId());
        settlementBillDTO.setOrderId(settlementBillEntity.getOrderId());
        settlementBillDTO.setSettlementAmount(settlementBillEntity.getTotalAmount());
        settlementBillDTO.setPaidAmount(settlementBillEntity.getTotalAmount());
        settlementBillDTO.setPaymentStatus(settlementBillEntity.getStatus());
        settlementBillDTO.setPaymentDate(settlementBillEntity.getUpdatedAt());

        return settlementBillDTO;
    }




}
