package org.edu.settlement.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

//@Configuration
//public class SettlementConfiguration {
//    @NacosValue(value = "${discounts}", autoRefreshed = true)
//    private String discountsJson;  // 这里接收 discounts 配置为 JSON 字符串
//
//    private Map<String, Double> discounts;  // 用于存储解析后的折扣信息
//
//    @NacosValue(value = "${shippingFee}", autoRefreshed = true)
//    private BigDecimal shippingFee;
//
//    @PostConstruct
//    public void init() {
//        try {
//            // 使用 Jackson 的 ObjectMapper 来解析 JSON 字符串为 Map
//            ObjectMapper objectMapper = new ObjectMapper();
//            discounts = objectMapper.readValue(discountsJson, new TypeReference<Map<String, Double>>() {});
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public Map<String, Double> getDiscounts() {
//        return discounts;
//    }
//
//    public BigDecimal getShippingFee() {
//        return shippingFee;
//    }
//}

@Component
@Data
public class SettlementConfiguration {
    @Getter
    @Value("${shippingFee}")
    private BigDecimal shippingFee;

    @Resource
    private DiscountConfig discountConfig;  // 用于存储解析后的折扣信息



//    @Value("${discounts}")
//    private String conf;

    @PostConstruct
    public void init() {
        try {
            //System.out.println(1111111111);
            System.out.println(shippingFee);
            //System.out.println(1111111111);
            //System.out.println(1111111111);
            System.out.println(discountConfig.getDiscounts());
            //System.out.println(1111111111);
              } catch (Exception e) {
                e.printStackTrace();
                }
    }




}



