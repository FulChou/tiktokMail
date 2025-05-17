package org.edu.settlement.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties()
public class DiscountConfig {

   private Map<Long, Double> discounts;

   public Map<Long, Double> getDiscounts() {
       return discounts;
   }

   public void setDiscounts(Map<Long, Double> discounts) {
       this.discounts = discounts;
   }
}