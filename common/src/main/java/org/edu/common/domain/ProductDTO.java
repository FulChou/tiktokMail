package org.edu.common.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class ProductDTO implements Serializable{
    private Long productId;
    private String productName;
    private String description;
    private BigDecimal price;
    private Integer categoryId;
    private String image;
    private Integer stockQuantity; // 商品数量
}
