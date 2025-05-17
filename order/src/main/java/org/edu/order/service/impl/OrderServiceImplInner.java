package org.edu.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.edu.order.infra.dao.OrderMapper;
import org.edu.order.infra.entity.OrderEntity;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImplInner extends ServiceImpl<OrderMapper, OrderEntity>{
}
