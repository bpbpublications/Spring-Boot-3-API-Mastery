package com.easyshop.orderservice.middleware.db.repo;

import com.easyshop.orderservice.middleware.db.entity.OrderEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.util.Optional;

public interface OrderRepository extends CrudRepository<OrderEntity, String>,
        QueryByExampleExecutor<OrderEntity> {

    Optional<OrderEntity> findByOrderCode(String orderCode);

}
