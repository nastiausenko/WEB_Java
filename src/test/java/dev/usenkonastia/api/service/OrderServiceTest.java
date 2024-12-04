package dev.usenkonastia.api.service;


import dev.usenkonastia.api.domain.order.Order;
import dev.usenkonastia.api.domain.order.OrderContext;
import dev.usenkonastia.api.repository.CosmoCatRepository;
import dev.usenkonastia.api.repository.OrderRepository;
import dev.usenkonastia.api.repository.entity.CosmoCatEntity;
import dev.usenkonastia.api.repository.entity.OrderEntity;
import dev.usenkonastia.api.service.exception.CatNotFoundException;
import dev.usenkonastia.api.service.impl.OrderServiceImpl;
import dev.usenkonastia.api.service.mapper.OrderMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Order Service Tests")
@SpringBootTest(classes = OrderServiceImpl.class)
public class OrderServiceTest {
    @Autowired
    private OrderService orderService;
    @MockBean
    private OrderRepository orderRepository;
    @MockBean
    private CosmoCatRepository cosmoCatRepository;
    @MockBean
    private OrderMapper orderMapper;
    private Order order;
    private OrderEntity orderEntity;
    private CosmoCatEntity cosmoCat;
    private static final String CART_ID = "a5b9bf50-f427-476c-9bec-d4c41c314d29";
    private OrderContext orderContext;


    @BeforeEach
    void setUp() {
        cosmoCat = CosmoCatEntity.builder()
                .catName("Sunny")
                .email("sunny@email.com")
                .build();
        order = Order.builder()
                .cartId(CART_ID)
                .customerReference("sunny@email.com")
                .totalPrice(12.3)
                .build();
        orderContext = OrderContext.builder()
                .customerReference("sunny@email.com")
                .cartId(CART_ID)
                .totalPrice(12.3)
                .build();
        orderEntity = OrderEntity.builder()
                .customer(cosmoCat)
                .cartId(CART_ID)
                .totalPrice(12.3)
                .build();
    }

    @Test
    void testPlaceOrder() {
        when(cosmoCatRepository.findByNaturalId(any())).thenReturn(Optional.of(cosmoCat));
        when(orderMapper.toOrderEntity(orderContext)).thenReturn(orderEntity);
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(orderEntity);
        when(orderMapper.toOrder(orderEntity)).thenReturn(order);

        Order placedOrder = orderService.placeOrder(orderContext);
        assertThat(placedOrder).isNotNull();
        assertThat(placedOrder.getCartId()).isEqualTo(CART_ID);

        verify(orderRepository).save(any(OrderEntity.class));
    }

    @Test
    void testPlaceOrderCatNotFound() {
        when(cosmoCatRepository.findByNaturalId(any())).thenReturn(Optional.empty());
        assertThrows(CatNotFoundException.class, () -> orderService.placeOrder(orderContext));
    }
}
