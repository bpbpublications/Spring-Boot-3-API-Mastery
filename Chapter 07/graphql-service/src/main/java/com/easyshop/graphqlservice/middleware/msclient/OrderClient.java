package com.easyshop.graphqlservice.middleware.msclient;

import com.easyshop.graphqlservice.dto.Order;
import com.easyshop.graphqlservice.middleware.msclient.dto.OrderPage;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface OrderClient {

    @GetExchange("/{orderCode}")
    Order findOrderByCode(@PathVariable String orderCode);

    @GetExchange
    OrderPage findOrders(@RequestParam Integer pageNumber,
                         @RequestParam Integer pageSize,
                         @RequestParam(required = false) String status);
}
