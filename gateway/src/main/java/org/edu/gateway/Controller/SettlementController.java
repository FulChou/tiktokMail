package org.edu.gateway.Controller;

import org.apache.dubbo.config.annotation.Reference;
import org.edu.common.domain.response.Result;
import org.edu.common.domain.settlement.*;
import org.edu.common.service.SettlementServiceRPC;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/settlement")
public class SettlementController {

    @Reference
    private SettlementServiceRPC settlementService;

    /**
     * 计算结算金额
     */
    @PostMapping("/calculateSettlementAmount")
    public Result<SettlementAmountDTO> calculateSettlementAmount(@RequestBody SettlementRequestDTO request) {
        try {
            SettlementAmountDTO result = settlementService.calculateSettlementAmount(request);
            return new Result.Builder().msg("结算金额计算成功").code(200).data(result).build();
        } catch (Exception e) {
            e.printStackTrace();
            return new Result.Builder().msg("计算结算金额失败：" + e.getMessage()).code(500).build();
        }
    }

    /**
     * 生成结算账单
     */
    @PostMapping("/generateSettlementBill")
    public Result<SettlementBillDTO> generateSettlementBill(@RequestBody SettlementRequestDTO request) {
        try {
            SettlementBillDTO result = settlementService.generateSettlementBill(request);
            return new Result.Builder().msg("结算账单生成成功").code(200).data(result).build();
        } catch (Exception e) {
            e.printStackTrace();
            return new Result.Builder().msg("生成结算账单失败：" + e.getMessage()).code(500).build();
        }
    }

    /**
     * 更新结算状态
     */
    @PutMapping("/updateSettlementStatus")
    public Result updateSettlementStatus(@RequestParam Long billId, @RequestParam Integer status) {
        try {
            boolean result = settlementService.updateSettlementStatus(billId, status);
            if (result) {
                return new Result.Builder().msg("结算状态更新成功").code(200).build();
            } else {
                return new Result.Builder().msg("更新结算状态失败，未找到相应账单").code(500).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result.Builder().msg("更新结算状态失败：" + e.getMessage()).code(500).build();
        }
    }

    /**
     * 处理退款请求
     */
    @PostMapping("/processRefund")
    public Result<RefundResponseDTO> processRefund(@RequestBody RefundRequestDTO request) {
        try {
            RefundResponseDTO result = settlementService.processRefund(request);
            return new Result.Builder().msg("退款请求处理成功").code(200).data(result).build();
        } catch (Exception e) {
            e.printStackTrace();
            return new Result.Builder().msg("处理退款请求失败：" + e.getMessage()).code(500).build();
        }
    }

    /**
     * 查询结算账单
     */
    @GetMapping("/getSettlementBill")
    public Result<SettlementBillDTO> getSettlementBill(@RequestParam Long billId) {
        try {
            SettlementBillDTO result = settlementService.getSettlementBill(billId);
            if (result != null) {
                return new Result.Builder().msg("查询结算账单成功").code(200).data(result).build();
            } else {
                return new Result.Builder().msg("未找到结算账单").code(404).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result.Builder().msg("查询结算账单失败：" + e.getMessage()).code(500).build();
        }
    }

}
