package org.edu.common.domain;


import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class OrderDto {
    private Long orderId; // 订单id (给系统用)
    private String orderNumber; // 订单编号（给用户看）
    private String Status; // 定义订单状态，比如：0未支付，1已支付，2已发货，3已完成等
    private Date createTime; // 订单创建时间
    private Date updateTime; // 订单更新时间
    private Long userId; // 用户id (用户表的主键，可获得用户名，电话等信息)
    private String username; // 用户名
    private String phone; // 用户电话
    private String address; // 用户地址
    private List<ProductDTO> products; // 订单中的商品列表（在数据库中，只需要存对应的商品id和对应的库存加减）
    private BigDecimal totalPrice; // 订单总价
    private Long paymentTransactionId; // 支付交易id
}
