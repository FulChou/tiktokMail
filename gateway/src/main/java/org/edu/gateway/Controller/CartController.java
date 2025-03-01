package org.edu.gateway.Controller;

import org.apache.dubbo.config.annotation.Reference;
import org.edu.common.domain.ProductDTO;
import org.edu.common.domain.response.Result;
import org.edu.common.service.CartServiceRPC;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Reference
    private CartServiceRPC cartService;

    @GetMapping("/list/{userId}")
    public Result<List<ProductDTO>> getCartList(@PathVariable Long userId) {
        try {
            List<ProductDTO> cartList = cartService.getCartList(userId);
            return new Result.Builder().msg("查询购物车成功").code(200).data(cartList).build();
        } catch (Exception e) {
            e.printStackTrace();
            return new Result.Builder().msg("查询购物车失败：" + e.getMessage()).code(500).build();
        }
    }

    @PostMapping("/add")
    public Result<Void> addProductToCart(@RequestParam Long userId, @RequestBody ProductDTO product) {
        try {
            cartService.addProductToCart(userId, product);
            return new Result.Builder().msg("添加商品到购物车成功").code(200).build();
        } catch (Exception e) {
            e.printStackTrace();
            return new Result.Builder().msg("添加商品到购物车失败：" + e.getMessage()).code(500).build();
        }
    }

    @DeleteMapping("/delete")
    public Result<Void> deleteProductFromCart(@RequestParam Long userId, @RequestBody ProductDTO product) {
        try {
            cartService.deleteProductFromCart(userId, product);
            return new Result.Builder().msg("从购物车中删除商品成功").code(200).build();
        } catch (Exception e) {
            e.printStackTrace();
            return new Result.Builder().msg("从购物车中删除商品失败：" + e.getMessage()).code(500).build();
        }
    }

    @DeleteMapping("/clear/{userId}")
    public Result<Void> clearCart(@PathVariable Long userId) {
        try {
            cartService.clearCart(userId);
            return new Result.Builder().msg("清空购物车成功").code(200).build();
        } catch (Exception e) {
            e.printStackTrace();
            return new Result.Builder().msg("清空购物车失败：" + e.getMessage()).code(500).build();
        }
    }
}
