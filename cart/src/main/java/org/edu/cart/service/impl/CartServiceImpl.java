package org.edu.cart.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.edu.cart.infra.dao.CartMapper;

import org.edu.cart.infra.entity.CartEntity;
import org.edu.common.domain.ProductDTO;
import org.edu.common.domain.request.ProductParam;
import org.edu.common.service.CartServiceRPC;
import org.edu.common.service.ProductServiceRPC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service(interfaceClass = CartServiceRPC.class)
public class CartServiceImpl implements CartServiceRPC {

    @Autowired
    private CartMapper cartMapper;

    @Reference
    private ProductServiceRPC productService;

    @Override
    public List<ProductDTO> getCartList(Long userId) {
        return cartMapper.getCartListWithProductInfo(userId);
    }

    @Override
    @Transactional
    public void addProductToCart(Long userId, ProductDTO product) {
        //检查商品库存
        ProductParam productParam = new ProductParam();
        productParam.setId(product.getProductId());
        ProductDTO productInfo = productService.getProductInfo(productParam);
        if (productInfo == null) {
            throw new RuntimeException("商品不存在");
        }
        if(productInfo.getStockQuantity() < product.getStockQuantity()){
            throw new RuntimeException("商品库存不足");
        }

        // 检查购物车中是否已有该用户和商品的记录
        QueryWrapper<CartEntity> cartEntityQueryWrapper = new QueryWrapper<>();
        cartEntityQueryWrapper.eq("user_id", userId)
                .eq("product_id", product.getProductId())
                .eq("deleted", false); // 仅查询未被软删除的记录

        CartEntity existingCartEntity = cartMapper.selectOne(cartEntityQueryWrapper);

        if (existingCartEntity != null) {
            // 如果购物车中已有该商品记录，更新商品数量
            existingCartEntity.setQuantity(existingCartEntity.getQuantity() + product.getStockQuantity());
            cartMapper.updateById(existingCartEntity);
        } else {
            // 如果购物车中没有该商品记录，插入新的购物车记录
            CartEntity cartEntity = new CartEntity();
            cartEntity.setUserId(userId);
            cartEntity.setProductId(product.getProductId());
            cartEntity.setQuantity(product.getStockQuantity());
            cartMapper.insert(cartEntity);
        }
    }

    @Override
    public void deleteProductFromCart(Long userId, ProductDTO product) {
        // 根据 userId 和 productId 查询购物车记录
        QueryWrapper<CartEntity> cartEntityQueryWrapper = new QueryWrapper<>();
        cartEntityQueryWrapper.eq("user_id", userId)
                                .eq("product_id", product.getProductId());
        CartEntity cartEntity = cartMapper.selectOne(cartEntityQueryWrapper);

        // 如果找到了对应的购物车记录
        if (cartEntity != null) {
            // 设置 deleted 属性为 true
            cartEntity.setDeleted(true);

            // 更新购物车记录
            cartMapper.updateById(cartEntity);
        } else {
            // 可以抛出异常或者记录日志，表示未找到对应的购物车记录
            throw new RuntimeException("购物车中未找到该商品");
        }
    }


    @Override
    public void clearCart(Long userId) {
        QueryWrapper<CartEntity> cartEntityQueryWrapper = new QueryWrapper<>();
        cartEntityQueryWrapper.eq("user_id", userId);
        List<CartEntity> cartEntities = cartMapper.selectList(cartEntityQueryWrapper);
        for (CartEntity cartEntity: cartEntities) {
            cartEntity.setDeleted(true);
            cartMapper.updateById(cartEntity);
        }
    }
}
