package org.edu.common.service;

import org.edu.common.domain.DatoDTO.OrderDataDTO;
import org.edu.common.domain.DatoDTO.OrderDetailDataDTO;
import org.edu.common.domain.OrderDto;

import java.util.List;


public interface OrderServiceRPC {

    /**
     * @param param
     * @apiNote 创建订单：根据前端接口传参，来创建一个订单。注意后台落订单表的时候，要和商品以及商品的库存和订单中商品的数量进行逻辑上的关联。
     * 比如说，考虑到如果商品库存不够，那么订单可能调用失败，即在商品服务中，就无法构建出A商品有X件这样的产品。
     * （进阶）建议思考分布式事务的实现方式。
     * @return 如果返回>0,则创建成功；
     */
    int createOrder(OrderDto param);

    /**
     * @param param
     * @apiNote 更新订单状态，可以更新订单内商品信息（买什么，买几个），用户信息（地址等），以及订单是否支付，是否完成的状态
     */
    int updateOrder(OrderDto param);

    /**
     * @param param
     * @apiNote :取消订单：用户手动取消订单,实现方式，变更订单状态即可
     */
    void cancel(OrderDto param);


    /**
     * @param param
     * @apiNote :删除订单（软删除），注意对应的商品库存也要进行相应的变更，还回去
     */
    void delete(OrderDto param);


    /**
     * @param param
     * @apiNote :订单支付，注意支付的时候，要校验订单的状态，以及支付金额是否和订单金额一致
     * （进阶）通过消息队列的方式来验证某个时间内是否支付成功，如果没有支付成功，那么就取消订单（订单模块接口可自行先写，我后续也会补上）
     */
    void verify(OrderDto param);

    OrderDataDTO getById(Long orderId);

    List<OrderDetailDataDTO> getOrderDetailsByOrderId(Long orderId);

    /**
     * 更新订单的对应的支付单ID
     * @param orderId
     * @param paymentTransactionId
     * @return
     */
    boolean updatePaymentTransactionId(Long orderId, Long paymentTransactionId);

    boolean updateStatus(Long orderId, Integer status);
}
