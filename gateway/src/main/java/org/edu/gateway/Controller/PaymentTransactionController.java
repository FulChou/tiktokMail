package org.edu.gateway.Controller;

import org.apache.dubbo.config.annotation.Reference;
import org.edu.common.domain.payment.PaymentOrderDTO;
import org.edu.common.domain.payment.PaymentRequestDTO;
import org.edu.common.domain.payment.PaymentResponseDTO;
import org.edu.common.domain.response.Result;
import org.edu.common.service.PaymentServiceRPC;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentTransactionController {

    @Reference
    private PaymentServiceRPC paymentService;

    /**
     * 创建支付订单
     */
    @PostMapping("/createPayment")
    public Result<PaymentOrderDTO> createPayment(@RequestBody PaymentRequestDTO request) {
        try {
            PaymentOrderDTO result = paymentService.createPayment(request);
            return new Result.Builder().msg("支付订单创建成功").code(200).data(result).build();
        } catch (Exception e) {
            e.printStackTrace();
            return new Result.Builder().msg("创建支付订单失败：" + e.getMessage()).code(500).build();
        }
    }

    /**
     * 执行支付
     */
    @PostMapping("/executePayment")
    public Result<PaymentResponseDTO> executePayment(@RequestParam Long paymentId) {
        try {
            PaymentResponseDTO result = paymentService.executePayment(paymentId);
            return new Result.Builder().msg("支付执行成功").code(200).data(result).build();
        } catch (Exception e) {
            e.printStackTrace();
            return new Result.Builder().msg("执行支付失败：" + e.getMessage()).code(500).build();
        }
    }

    /**
     * 取消支付（用户主动取消）
     */
    @PostMapping("/cancelPayment")
    public Result<Boolean> cancelPayment(@RequestParam Long paymentId) {
        try {
            boolean result = paymentService.cancelPayment(paymentId);
            if (result) {
                return new Result.Builder().msg("支付取消成功").code(200).data(result).build();
            } else {
                return new Result.Builder().msg("支付取消失败，支付状态已更新").code(500).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result.Builder().msg("取消支付失败：" + e.getMessage()).code(500).build();
        }
    }

    /**
     * 定时取消支付（超时未支付自动取消）
     */
    @PostMapping("/autoCancelUnpaidPayments")
    public Result<Void> autoCancelUnpaidPayments() {
        try {
            paymentService.autoCancelUnpaidPayments();
            return new Result.Builder().msg("定时取消未支付支付订单成功").code(200).build();
        } catch (Exception e) {
            e.printStackTrace();
            return new Result.Builder().msg("定时取消未支付支付订单失败：" + e.getMessage()).code(500).build();
        }
    }

    /**
     * 查询支付状态
     */
    @GetMapping("/getPaymentStatus")
    public Result<PaymentOrderDTO> getPaymentStatus(@RequestParam Long paymentId) {
        try {
            PaymentOrderDTO result = paymentService.getPaymentStatus(paymentId);
            return new Result.Builder().msg("支付状态查询成功").code(200).data(result).build();
        } catch (Exception e) {
            e.printStackTrace();
            return new Result.Builder().msg("查询支付状态失败：" + e.getMessage()).code(500).build();
        }
    }
}
