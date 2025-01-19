package org.edu.commom.domain;

public class ProductDTO {
    private String id;
    private String name;
    private String description;
    private String price;
    private String category;
    private String image;

    public ProductDTO(String id, String name, String description, String price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }
}
