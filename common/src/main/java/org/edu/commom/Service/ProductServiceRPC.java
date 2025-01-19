package org.edu.commom.Service;

import org.edu.commom.domain.ProductDTO;
import org.edu.commom.domain.request.ProductParam;

import java.util.List;

public interface ProductServiceRPC {

    /**
     * @param product 保存商品
     * @apiNote 对于管理员和商家，可以通过后台管理姐妹上传商品，比如说商品的名称，描述，价格，类别，图片等，以及商品的库存等信息
     * @return void
     */
    void saveProduct(ProductDTO product);

    /**
     * @param product 更新商品和商品信息
     * @apiNote 更新商品的图片，价格，描述，类别等信息，图片可以使用图片上传接口上传图片，然后将图片的url保存到商品信息中（涉及对象存储，可后续扩展）
     */
    void updateProduct(ProductDTO product);


    /**
     * @param product 删除商品
     * @apiNote ：软删除商品，将商品的状态设置为删除状态，不再展示给用户，但是不会从数据库中删除
     */
    void deleteProduct(ProductDTO product);


    /**
     * @param param 查询商品列表
     * @return 可以根据商品的名称，类别，价格等信息查询商品列表，可以分页查询，每页展示10条数据，可以做模糊查询（名字）
     */
    List<ProductDTO> getProductList(ProductParam param);

    /**
     * @param param 查询单一商品详情
     * @return 查询商品的详细信息，包括商品的名称，描述，价格，类别，图片等信息
     */
    ProductDTO getProductInfo(ProductParam param);

}
