package org.edu.gateway.Controller;

import org.apache.dubbo.config.annotation.Reference;
import org.edu.common.domain.OrderDto;
import org.edu.common.domain.response.Result;
import org.edu.common.service.OrderServiceRPC;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Reference
    private OrderServiceRPC orderService;

    @PostMapping("/create")
    public Result createOrder(@RequestBody OrderDto order) {
        try {
            int result = orderService.createOrder(order);
            if (result > 0) {
                return new Result.Builder().msg("订单创建成功").code(200).build();
            }else {
                return new Result.Builder().msg("库存不足").code(500).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result.Builder().msg("创建订单失败：" + e.getMessage()).code(500).build();
        }
    }

    @PutMapping("/update")
    public Result updateOrder(@RequestBody OrderDto order) {
        try {
            int result = orderService.updateOrder(order);
            if (result > 0) {
                return new Result.Builder().msg("订单更新成功").code(200).build();
            }else {
                return new Result.Builder().msg("订单更新失败").code(500).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result.Builder().msg("订单更新失败：" + e.getMessage()).code(500).build();
        }
    }

    @PutMapping("/cancel/{id}")
    public Result<Void> cancelOrder(@PathVariable Long id) {
        try {
            OrderDto order = new OrderDto();
            order.setOrderId(id);
            orderService.cancel(order);
            return new Result.Builder().msg("订单取消成功").code(200).build();
        } catch (Exception e) {
            e.printStackTrace();
            return new Result.Builder().msg("订单取消失败：" + e.getMessage()).code(500).build();
        }
    }

    @PutMapping("/delete/{id}")
    public Result<Void> deleteOrder(@PathVariable Long id) {
        try {
            OrderDto order = new OrderDto();
            order.setOrderId(id);
            orderService.delete(order);
            return new Result.Builder().msg("订单删除成功").code(200).build();
        } catch (Exception e) {
            e.printStackTrace();
            return new Result.Builder().msg("订单删除失败：" + e.getMessage()).code(500).build();
        }
    }

    @PutMapping("/verify/{id}")
    public Result<Void> verifyOrder(@PathVariable Long id) {
        try {
            OrderDto order = new OrderDto();
            order.setOrderId(id);
            orderService.verify(order);
            return new Result.Builder().msg("订单审核成功").code(200).build();
        } catch (Exception e) {
            e.printStackTrace();
            return new Result.Builder().msg("订单审核失败：" + e.getMessage()).code(500).build();
        }
    }



}
