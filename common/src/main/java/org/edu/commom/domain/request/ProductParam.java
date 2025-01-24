package org.edu.commom.domain.request;

import lombok.Data;

import java.math.BigDecimal;
import java.io.Serializable;

@Data
public class ProductParam extends baseReq implements Serializable{

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer category;
    private String image;
}
