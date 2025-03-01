package org.edu.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.edu.common.constant.OrderStatusConstants;
import org.edu.common.domain.DatoDTO.InventoryDataDTO;
import org.edu.common.domain.DatoDTO.OrderDataDTO;
import org.edu.common.domain.DatoDTO.OrderDetailDataDTO;
import org.edu.common.domain.DatoDTO.UserDataDTO;
import org.edu.common.domain.OrderDto;
import org.edu.common.domain.ProductDTO;
import org.edu.common.service.OrderServiceRPC;
import org.edu.common.service.ProductServiceRPC;
import org.edu.common.service.UserServiceRPC;
import org.edu.order.infra.dao.OrderDetailMapper;
import org.edu.order.infra.dao.OrderMapper;
import org.edu.order.infra.entity.OrderDetailEntity;
import org.edu.order.infra.entity.OrderEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service(interfaceClass = OrderServiceRPC.class)
public class OrderServiceImpl implements OrderServiceRPC {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Reference
    private ProductServiceRPC productService;

    @Reference
    private UserServiceRPC userService;

    public OrderDataDTO getById(Long id) {
        OrderEntity orderEntity = orderMapper.selectById(id);

        OrderDataDTO orderDataDTO = new OrderDataDTO();
        orderDataDTO.setId(orderEntity.getId());
        orderDataDTO.setOrderNumber(orderEntity.getOrderNumber());
        orderDataDTO.setUserId(orderEntity.getUserId());
        orderDataDTO.setTotalPrice(orderEntity.getTotalPrice());
        orderDataDTO.setPaymentTransactionId(orderEntity.getPaymentTransactionId());
        orderDataDTO.setStatus(orderEntity.getStatus());
        orderDataDTO.setDeleted(orderEntity.getDeleted());
        orderDataDTO.setCreatedAt(orderEntity.getCreatedAt());
        orderDataDTO.setUpdatedAt(orderEntity.getUpdatedAt());

        return orderDataDTO;
    }

    @Override
    public List<OrderDetailDataDTO> getOrderDetailsByOrderId(Long orderId) {
        List<OrderDetailEntity> orderDetails = orderDetailMapper.selectList(new QueryWrapper<OrderDetailEntity>().eq("order_id", orderId));

        List<OrderDetailDataDTO> orderDetailDataDTOS = new ArrayList<>();
        for (OrderDetailEntity orderDetailEntity : orderDetails) {
            OrderDetailDataDTO orderDetailDataDTO = new OrderDetailDataDTO();
            orderDetailDataDTO.setId(orderDetailEntity.getId());
            orderDetailDataDTO.setOrderId(orderDetailEntity.getOrderId());
            orderDetailDataDTO.setProductId(orderDetailEntity.getProductId());
            orderDetailDataDTO.setQuantity(orderDetailEntity.getQuantity());
            orderDetailDataDTO.setPrice(orderDetailEntity.getPrice());
            orderDetailDataDTO.setCreatedAt(orderDetailEntity.getCreatedAt());
            orderDetailDataDTO.setUpdatedAt(orderDetailEntity.getUpdatedAt());
            orderDetailDataDTOS.add(orderDetailDataDTO);
        }
        return orderDetailDataDTOS;
    }

    @Override
    public boolean updatePaymentTransactionId(Long orderId, Long paymentTransactionId) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(orderId);
        orderEntity.setPaymentTransactionId(paymentTransactionId);
        return orderMapper.updateById(orderEntity) > 0;
    }

    @Override
    public boolean updateStatus(Long orderId, Integer status) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(orderId);
        orderEntity.setStatus(status);
        return orderMapper.updateById(orderEntity) > 0;
    }


    @Override
    @Transactional
    public int createOrder(OrderDto param) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderNumber(param.getOrderNumber());
        orderEntity.setUserId(param.getUserId());
        orderEntity.setTotalPrice(param.getTotalPrice());
        orderEntity.setPaymentTransactionId(param.getPaymentTransactionId());
        orderEntity.setStatus(param.getStatus());
        orderMapper.insert(orderEntity);

        for (int i = 0; i < param.getProducts().size(); i++) {
            OrderDetailEntity orderDetailEntity = new OrderDetailEntity();
            //判断商品库存是否足够，使用inventory表
//            QueryWrapper<InventoryEntity> inventoryEntityQueryWrapper =  new QueryWrapper<>();
//            inventoryEntityQueryWrapper.eq("product_id", param.getProducts().get(i).getProductId());
//            InventoryEntity inventoryEntity =  inventoryMapper.selectOne(inventoryEntityQueryWrapper);
//
//            if (inventoryEntity.getStockQuantity() < param.getProducts().get(i).getStockQuantity()) {
//                return -1;
//            }else{
//                inventoryEntity.setStockQuantity(inventoryEntity.getStockQuantity() - param.getProducts().get(i).getStockQuantity());
//            }
            InventoryDataDTO inventoryDataDTO = productService.getInventoryByProductId(param.getProducts().get(i).getProductId());
            if (inventoryDataDTO.getStockQuantity() < param.getProducts().get(i).getStockQuantity()) {
                return 0;
            } else {
                inventoryDataDTO.setStockQuantity(inventoryDataDTO.getStockQuantity() - param.getProducts().get(i).getStockQuantity());
                productService.updateInventory(inventoryDataDTO);
            }
            orderDetailEntity.setOrderId(orderEntity.getId());
            orderDetailEntity.setProductId(param.getProducts().get(i).getProductId());
            orderDetailEntity.setQuantity(param.getProducts().get(i).getStockQuantity());
            orderDetailEntity.setPrice(param.getProducts().get(i).getPrice());
            orderDetailMapper.insert(orderDetailEntity);
        }

        if(param.getUserId() != null){
            UserDataDTO userDataDTO = userService.getById(param.getUserId());
            userDataDTO.setUsername(param.getUsername());
            userDataDTO.setPhone(param.getPhone());
            userDataDTO.setAddress(param.getAddress());
            userService.updateById(userDataDTO);
        }
        return 1;
    }

    @Override
    @Transactional
    public int updateOrder(OrderDto param) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(param.getOrderId());
        orderEntity.setOrderNumber(param.getOrderNumber());
        orderEntity.setUserId(param.getUserId());
        orderEntity.setTotalPrice(param.getTotalPrice());
        orderEntity.setPaymentTransactionId(param.getPaymentTransactionId());
        orderEntity.setStatus(param.getStatus());
        orderMapper.updateById(orderEntity);

        for (int i = 0; i < param.getProducts().size(); i++) {
            OrderDetailEntity orderDetailEntity = new OrderDetailEntity();
            // 判断商品库存是否足够，使用inventory表
//            QueryWrapper<InventoryEntity> inventoryEntityQueryWrapper =  new QueryWrapper<>();
//            inventoryEntityQueryWrapper.eq("product_id", param.getProducts().get(i).getProductId());
//            InventoryEntity inventoryEntity =  inventoryMapper.selectOne(inventoryEntityQueryWrapper);
//            if (inventoryEntity.getStockQuantity() < param.getProducts().get(i).getStockQuantity()) {
//                return -1;
//            }else{
//                inventoryEntity.setStockQuantity(inventoryEntity.getStockQuantity() - param.getProducts().get(i).getStockQuantity());
//            }
            InventoryDataDTO inventoryDataDTO = productService.getInventoryByProductId(param.getProducts().get(i).getProductId());
            if (inventoryDataDTO.getStockQuantity() < param.getProducts().get(i).getStockQuantity()) {
                return 0;
            } else {
                inventoryDataDTO.setStockQuantity(inventoryDataDTO.getStockQuantity() - param.getProducts().get(i).getStockQuantity());
                productService.updateInventory(inventoryDataDTO);
            }

            // 查询该商品是否已存在在订单明细中
            QueryWrapper<OrderDetailEntity> orderDetailQueryWrapper = new QueryWrapper<>();
            orderDetailQueryWrapper.eq("order_id", param.getOrderId())
                                    .eq("product_id", param.getProducts().get(i).getProductId());
            OrderDetailEntity existingOrderDetail = orderDetailMapper.selectOne(orderDetailQueryWrapper);

            if (existingOrderDetail != null) {
                // 如果记录存在，更新数量
                existingOrderDetail.setQuantity(param.getProducts().get(i).getStockQuantity());
                orderDetailMapper.updateById(existingOrderDetail);
            } else {
                // 如果记录不存在，插入新记录
                orderDetailEntity.setOrderId(param.getOrderId());
                orderDetailEntity.setProductId(param.getProducts().get(i).getProductId());
                orderDetailEntity.setQuantity(param.getProducts().get(i).getStockQuantity());
                orderDetailEntity.setPrice(param.getProducts().get(i).getPrice());
                orderDetailMapper.insert(orderDetailEntity);
            }
        }

        if(param.getUserId() != null){
            UserDataDTO userDataDTO = userService.getById(param.getUserId());
            userDataDTO.setUsername(param.getUsername());
            userDataDTO.setPhone(param.getPhone());
            userDataDTO.setAddress(param.getAddress());
            userService.updateById(userDataDTO);
        }

        return 1;
    }

    @Override
    @Transactional
    public void cancel(OrderDto param) {
        //更新订单状态为已取消
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(param.getOrderId());
        orderEntity.setStatus(OrderStatusConstants.CANCELLED);
        orderMapper.updateById(orderEntity);

        //查询订单明细并恢复库存
        QueryWrapper<OrderDetailEntity> orderDetailQueryWrapper = new QueryWrapper<>();
        orderDetailQueryWrapper.eq("order_id", param.getOrderId());
        List<OrderDetailEntity> orderDetailList = orderDetailMapper.selectList(orderDetailQueryWrapper);

        for (OrderDetailEntity orderDetail : orderDetailList) {

            InventoryDataDTO inventoryDTO = productService.getInventoryByProductId(orderDetail.getProductId());

            if (inventoryDTO != null) {
                //恢复库存
                inventoryDTO.setStockQuantity(inventoryDTO.getStockQuantity() + orderDetail.getQuantity());
                productService.updateInventory(inventoryDTO);
            }
        }
    }

    @Override
    @Transactional
    public void delete(OrderDto param) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(param.getOrderId());
        orderEntity.setDeleted(true);
        orderMapper.updateById(orderEntity);

        //在这里找出orderId对应的order_detail表，恢复对应库存
        //查询订单明细并恢复库存
        QueryWrapper<OrderDetailEntity> orderDetailQueryWrapper = new QueryWrapper<>();
        orderDetailQueryWrapper.eq("order_id", param.getOrderId());
        List<OrderDetailEntity> orderDetailList = orderDetailMapper.selectList(orderDetailQueryWrapper);

        for (OrderDetailEntity orderDetail : orderDetailList) {

            InventoryDataDTO inventoryDTO = productService.getInventoryByProductId(orderDetail.getProductId());

            if (inventoryDTO != null) {
                //恢复库存
                inventoryDTO.setStockQuantity(inventoryDTO.getStockQuantity() + orderDetail.getQuantity());
                productService.updateInventory(inventoryDTO);
            }
        }
//        for(int i = 0; i < param.getProducts().size(); i++){
//            //删除订单中的商品，更新inventory表数据
//            InventoryDataDTO inventoryDTO = productService.getInventoryByProductId(param.getProducts().get(i).getProductId());
//            if (inventoryDTO != null) {
//                //恢复库存
//                inventoryDTO.setStockQuantity(inventoryDTO.getStockQuantity() + param.getProducts().get(i).getStockQuantity());
//                productService.updateInventory(inventoryDTO);
//            }
//            //不用真正删除订单详细表中的数据
////            orderDetailEntity.setId(param.getProducts().get(i).getProductId());
////            orderDetailMapper.deleteById(orderDetailEntity);
//        }
    }

    @Override
    public void verify(OrderDto param) {
        // 验证订单状态
        if (param.getStatus() != OrderStatusConstants.PENDING_PAYMENT) {
            // 如果订单不是 PENDING_PAYMENT 状态
            // 则不能再次进行支付操作
            throw new IllegalStateException("订单已处理，不能继续支付。");
        }

        // 创建结算请求对象 SettlementRequestDTO
//        SettlementRequestDTO request = new SettlementRequestDTO();
//        request.setOrderId(param.getOrderId());  // 设置订单ID
//        request.setUserId(param.getUserId());    // 设置用户ID

        // 生成结算单（加入会导致循环依赖）
        //SettlementBillDTO settlementBill = settlementService.generateSettlementBill(request);

        // 验证支付金额是否与订单总额一致
//        if (settlementBill == null || settlementBill.getPaidAmount() == null) {
//            throw new IllegalStateException("生成结算单时发生错误。");
//        }
    }

}
