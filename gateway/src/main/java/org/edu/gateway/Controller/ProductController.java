package org.edu.gateway.Controller;

import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.config.annotation.Reference;
import org.edu.common.service.ProductServiceRPC;
import org.edu.common.domain.ProductDTO;
import org.edu.common.domain.response.Result;
import org.edu.common.domain.request.ProductParam;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Reference
    private ProductServiceRPC productService;

    @PostMapping(value = "/create")
    public Result createProduct(@RequestBody ProductDTO product) {
        try {
            productService.saveProduct(product);
            return new Result.Builder().msg("商品创建成功").code(200).build();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("创建商品失败", e);
            return new Result.Builder().msg("创建商品失败：" + e.getMessage()).code(500).build();
        }
    }

    @PutMapping(value = "/update")
    public Result updateProduct(@RequestBody ProductDTO product) {
        try {
            productService.updateProduct(product);
            return new Result.Builder().msg("商品更新成功").code(200).build();
        } catch (Exception e) {
            logger.error("更新商品失败", e);
            return new Result.Builder().msg("更新商品失败：" + e.getMessage()).code(500).build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteProduct(@PathVariable Long id) {
        try {
            ProductDTO product = new ProductDTO();
            product.setProductId(id);
            productService.deleteProduct(product);
            return new Result.Builder().msg("商品删除成功").code(200).build();
        } catch (Exception e) {
            logger.error("删除商品失败", e);
            return new Result.Builder().msg("删除商品失败：" + e.getMessage()).code(500).build();
        }
    }

    @GetMapping("/list")
    public Result<List<ProductDTO>> getProductList(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer category,
            @RequestParam(required = false) BigDecimal price,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        try {
            ProductParam param = new ProductParam();
            param.setName(name);
            param.setCategory(category);
            param.setPrice(price);
            param.setPageNum(pageNum);
            param.setPageSize(pageSize);

            List<ProductDTO> products = productService.getProductList(param);
            return new Result.Builder().msg("查询商品成功").code(200).data(products).build();
        } catch (Exception e) {
            logger.error("查询商品失败", e);
            return new Result.Builder().msg("查询商品失败：" + e.getMessage()).code(500).build();
        }
    }

    @GetMapping("/{id}")
    public Result<ProductDTO> getProductInfo(@PathVariable Long id) {
        try {
            ProductParam param = new ProductParam();
            param.setId(id);
            ProductDTO product = productService.getProductInfo(param);
            if (product == null) {
                return new Result.Builder().msg("商品不存在").code(404).build();
            }
            return new Result.Builder().msg("查询成功").code(200).data(product).build();
        } catch (Exception e) {
            logger.error("获取商品详情失败", e);
            return new Result.Builder().msg("获取商品详情失败：" + e.getMessage()).code(500).build();
        }
    }
}