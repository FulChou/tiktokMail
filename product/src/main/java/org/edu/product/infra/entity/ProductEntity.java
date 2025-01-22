package org.edu.product.infra.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;



import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;

@Data
@TableName("products")
public class ProductEntity {
    @TableId(type = IdType.AUTO)
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
