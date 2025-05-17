package org.edu.product.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.edu.product.infra.dao.InventoryMapper;
import org.edu.product.infra.entity.InventoryEntity;
import org.springframework.stereotype.Service;

@Service
public class InventoryService extends ServiceImpl<InventoryMapper, InventoryEntity> {
}
