package org.edu.commom.domain.request;

import lombok.Data;

@Data
public class ProductParam extends baseReq {

    private String id;
    private String name;
    private String description;
    private String price;
    private String category;
    private String image;
}
