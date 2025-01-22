package org.edu.product.infra.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.edu.product.infra.entity.InventoryEntity;

@Mapper
public interface InventoryMapper extends BaseMapper<InventoryEntity> {

}
