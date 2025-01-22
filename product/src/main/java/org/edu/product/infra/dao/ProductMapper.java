package org.edu.product.infra.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.edu.product.infra.entity.ProductEntity;

@Mapper
public interface ProductMapper extends BaseMapper<ProductEntity> {

}
