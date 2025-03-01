package org.edu.cart.infra.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.edu.cart.infra.entity.CartEntity;
import org.edu.common.domain.ProductDTO;

import java.util.List;

@Mapper
public interface CartMapper extends BaseMapper<CartEntity>{

    List<ProductDTO> getCartListWithProductInfo(@Param("userId") Long userId);
}
