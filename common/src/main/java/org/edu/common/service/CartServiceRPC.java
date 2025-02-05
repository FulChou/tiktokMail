package org.edu.common.service;

import org.edu.common.domain.ProductDTO;

import java.util.List;

public interface CartServiceRPC {

    // 购物车中有一系列商品：
    List<ProductDTO> getCartList(Long userId);

    // 添加商品到购物车：
    void addProductToCart(Long userId, ProductDTO product);

    // 从购物车中删除商品：
    void deleteProductFromCart(Long userId, ProductDTO product);

    // 清空购物车：
    void clearCart(Long userId);

}
