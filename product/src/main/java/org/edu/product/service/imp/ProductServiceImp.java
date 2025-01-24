package org.edu.product.service.imp;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.dubbo.config.annotation.Service;
import org.edu.commom.Service.ProductServiceRPC;
import org.edu.commom.domain.ProductDTO;
import org.edu.commom.domain.request.ProductParam;
import org.edu.product.infra.dao.InventoryMapper;
import org.edu.product.infra.dao.ProductMapper;
import org.edu.product.infra.entity.InventoryEntity;
import org.edu.product.infra.entity.ProductEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.edu.commom.Constant.Constants.INVALID;
import static org.edu.commom.Constant.Constants.VALID;

@Service(interfaceClass = ProductServiceRPC.class)
public class ProductServiceImp extends ServiceImpl<ProductMapper, ProductEntity> implements ProductServiceRPC {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private InventoryMapper inventoryMapper;

    @Transactional
    @Override
    public void saveProduct(ProductDTO product) {
        //创建 ProductEntity 对象并设置属性
        ProductEntity productEntity = new ProductEntity();
        productEntity.setProductName(product.getProductName());
        productEntity.setDescription(product.getDescription());
        productEntity.setPrice(product.getPrice());
        productEntity.setCategoryId(product.getCategoryId());
        productEntity.setImage(product.getImage());
        productEntity.setStatus(VALID);

        //插入 ProductEntity 到数据库
        productMapper.insert(productEntity); // 插入商品记录

        //创建 InventoryEntity 对象并设置库存
        InventoryEntity inventoryEntity = new InventoryEntity();
        inventoryEntity.setProductId(productEntity.getProductId()); // 使用插入后的商品 ID
        inventoryEntity.setStockQuantity(product.getStockQuantity()); // 设置库存数量

        //判断库存是否已存在
        QueryWrapper<InventoryEntity> inventoryEntityQueryWrapper = new QueryWrapper<>();

        inventoryEntityQueryWrapper.eq("product_id", productEntity.getProductId());

        InventoryEntity existingInventory = inventoryMapper.selectOne(inventoryEntityQueryWrapper);

        if (existingInventory != null) {
            // 库存记录已存在，更新库存
            existingInventory.setStockQuantity(existingInventory.getStockQuantity() + product.getStockQuantity());
            inventoryMapper.updateById(existingInventory);
        } else {
            // 库存记录不存在，插入新库存记录
            inventoryMapper.insert(inventoryEntity);
        }

    }

    @Transactional
    @Override
    public void updateProduct(ProductDTO product) {
        //更新 ProductEntity 商品信息
        ProductEntity productEntity = new ProductEntity();
        productEntity.setProductId(product.getProductId());  // 设置商品ID
        productEntity.setProductName(product.getProductName());
        productEntity.setDescription(product.getDescription());
        productEntity.setPrice(product.getPrice());
        productEntity.setCategoryId(product.getCategoryId());
        productEntity.setImage(product.getImage());
        productEntity.setStatus(VALID);

        // 更新商品信息
        productMapper.updateById(productEntity);

        //更新库存信息
        // 获取现有库存记录
        Long productId = productEntity.getProductId();

        QueryWrapper<InventoryEntity> inventoryEntityQueryWrapper = new QueryWrapper<>();

        inventoryEntityQueryWrapper.eq("product_id", productId);

        InventoryEntity inventoryEntity = inventoryMapper.selectOne(inventoryEntityQueryWrapper);

        if (inventoryEntity != null) {
            // 如果库存记录存在，则更新库存
            inventoryEntity.setStockQuantity(product.getStockQuantity());
            inventoryMapper.updateById(inventoryEntity); // 更新库存
        }
    }

    @Override
    public void deleteProduct(ProductDTO product) {
        //软删除商品，将商品的状态设置为删除状态，不再展示给用户，但是不会从数据库中删除
        ProductEntity productEntity = new ProductEntity();
        productEntity.setProductId(product.getProductId());
        productEntity.setStatus(INVALID);
        productMapper.updateById(productEntity);
    }

    @Override
    public List<ProductDTO> getProductList(ProductParam param) {
        //可以根据商品的名称，类别，价格等信息查询商品列表，可以分页查询，每页展示10条数据，可以做模糊查询（名字）
        // 设置分页信息，默认每页10条
        int pageNum = param.getPageNum();
        int pageSize = param.getPageSize();
        Page<ProductEntity> page = new Page<>(pageNum, pageSize);

        // 构建查询条件
        QueryWrapper<ProductEntity> queryWrapper = new QueryWrapper<>();

        // 商品名称模糊查询
        if (param.getName() != null && !param.getName().isEmpty()) {
            queryWrapper.like("product_name", param.getName());
        }

        // 商品类别查询
        if (param.getCategory() != null) {
            queryWrapper.eq("category_id", param.getCategory());
        }

        //价格查询
        if(param.getPrice() != null){
            queryWrapper.eq("price", param.getPrice());
        }

        // 执行分页查询
        Page<ProductEntity> productPage = this.page(page, queryWrapper);

        // 将查询结果转化为 DTO 列表
        List<ProductDTO> productDTOList = productPage.getRecords().stream()
                .map(product -> {
                    ProductDTO dto = new ProductDTO();
                    dto.setProductId(product.getProductId());
                    dto.setProductName(product.getProductName());
                    dto.setCategoryId(product.getCategoryId());
                    dto.setDescription(product.getDescription());
                    dto.setPrice(product.getPrice());
                    return dto;
                })
                .collect(Collectors.toList());

        return productDTOList;
    }

    @Override
    public ProductDTO getProductInfo(ProductParam param) {
        if (param.getId() != null) {
            ProductEntity productEntity = productMapper.selectById(param.getId());

            QueryWrapper<InventoryEntity> inventoryEntityQueryWrapper = new QueryWrapper<>();

            inventoryEntityQueryWrapper.eq("product_id", param.getId());

            InventoryEntity inventoryEntity = inventoryMapper.selectOne(inventoryEntityQueryWrapper);

            ProductDTO productDTO = new ProductDTO();
            productDTO.setProductId(productEntity.getProductId());
            productDTO.setProductName(productEntity.getProductName());
            productDTO.setDescription(productEntity.getDescription());
            productDTO.setPrice(productEntity.getPrice());
            productDTO.setCategoryId(productEntity.getCategoryId());
            productDTO.setStockQuantity(inventoryEntity.getStockQuantity());
            return productDTO;
        }
        return null;
    }
}
