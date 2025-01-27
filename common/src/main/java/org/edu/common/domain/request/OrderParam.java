package org.edu.common.domain.request;

import lombok.Data;
import org.edu.common.domain.ProductDTO;

import java.util.List;

// 订单服务Restful服务参数
@Data
public class OrderParam {
    private Long userId; // 创建人
    private List<ProductDTO> products; // 订单内包含商品
    private String  address;
}
