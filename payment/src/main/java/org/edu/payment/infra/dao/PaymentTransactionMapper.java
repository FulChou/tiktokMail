package org.edu.payment.infra.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.edu.payment.infra.entity.PaymentTransactionEntity;

@Mapper
public interface PaymentTransactionMapper extends BaseMapper<PaymentTransactionEntity> {
}
