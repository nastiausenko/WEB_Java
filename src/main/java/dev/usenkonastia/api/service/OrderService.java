package dev.usenkonastia.api.service;

import dev.usenkonastia.api.domain.order.Order;
import dev.usenkonastia.api.domain.order.OrderContext;

public interface OrderService {
    Order placeOrder(OrderContext orderContext);
}
