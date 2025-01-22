package org.edu.product.infra.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("inventory")
public class InventoryEntity {
    @TableId(type = IdType.AUTO)
    Long inventoryId;
    Long productId;
    Integer stockQuantity;
    Integer lockedQuantity;
    Integer lowStockThreshold;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

}
