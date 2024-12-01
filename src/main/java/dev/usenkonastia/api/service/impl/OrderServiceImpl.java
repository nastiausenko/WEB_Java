package dev.usenkonastia.api.service.impl;

import dev.usenkonastia.api.domain.order.Order;
import dev.usenkonastia.api.domain.order.OrderContext;
import dev.usenkonastia.api.repository.CosmoCatRepository;
import dev.usenkonastia.api.repository.OrderRepository;
import dev.usenkonastia.api.repository.entity.CosmoCatEntity;
import dev.usenkonastia.api.repository.entity.OrderEntity;
import dev.usenkonastia.api.repository.entity.OrderItemEntity;
import dev.usenkonastia.api.service.OrderService;
import dev.usenkonastia.api.service.exception.CatNotFoundException;
import dev.usenkonastia.api.service.mapper.OrderMapper;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final CosmoCatRepository cosmoCatRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public Order placeOrder(OrderContext orderContext) {
        log.info("Placing an order for cart: {} and customer: {}", orderContext.getCartId(), orderContext.getCustomerReference());

        CosmoCatEntity customer = cosmoCatRepository.findByNaturalId(orderContext.getCustomerReference())
                .orElseThrow(() -> new CatNotFoundException(orderContext.getCustomerReference()));

        try {
            OrderEntity orderEntity = orderMapper.toOrderEntity(orderContext);
            List<OrderItemEntity> orderItemEntities = orderMapper.toOrderItemEntities(orderContext.getProducts(), orderEntity);
            orderEntity.setCustomer(customer);
            orderEntity.setProducts(orderItemEntities);
            return orderMapper.toOrder(orderRepository.save(orderEntity));

        } catch (Exception e) {
            throw new PersistenceException(e.getMessage());
        }
    }
}

