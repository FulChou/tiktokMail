package org.edu.common.domain.DatoDTO;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class InventoryDataDTO implements Serializable {
    Long inventoryId;
    Long productId;
    Integer stockQuantity;
    Integer lockedQuantity;
    Integer lowStockThreshold;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
