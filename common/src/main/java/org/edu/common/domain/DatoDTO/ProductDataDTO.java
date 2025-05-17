package org.edu.common.domain.DatoDTO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductDataDTO implements Serializable {
    private Long productId;
    private String productName;
    private String description;
    private String image;
    private BigDecimal price;
    private Integer categoryId;
    private Integer brandId; //
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
