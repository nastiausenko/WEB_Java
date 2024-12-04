package dev.usenkonastia.api.service.mapper;

import dev.usenkonastia.api.domain.order.Order;
import dev.usenkonastia.api.domain.order.OrderContext;
import dev.usenkonastia.api.domain.order.OrderItem;
import dev.usenkonastia.api.dto.order.OrderItemDto;
import dev.usenkonastia.api.dto.order.PlaceOrderRequestDto;
import dev.usenkonastia.api.dto.order.PlaceOrderResponseDto;
import dev.usenkonastia.api.repository.entity.OrderEntity;
import dev.usenkonastia.api.repository.entity.OrderItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "cartId", source = "cartId")
    @Mapping(target = "totalPrice", source = "orderDto.totalPrice")
    @Mapping(target = "customerReference", source = "customerReference")
    @Mapping(target = "products", source = "orderDto.products")
    OrderContext toOrderContext(String cartId, String customerReference, PlaceOrderRequestDto orderDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "cartId", target = "cartId")
    @Mapping(target = "paymentReference", expression = "java(java.util.UUID.randomUUID().toString())")
    @Mapping(source = "customerReference", target = "customer.email")
    @Mapping(source = "products", target = "products")
    OrderEntity toOrderEntity(OrderContext orderContext);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "cartId", target = "cartId")
    @Mapping(source = "customer.email", target = "customerReference")
    @Mapping(source = "paymentReference", target = "transactionId")
    @Mapping(source = "products", target = "products")
    @Mapping(source = "totalPrice", target = "totalPrice")
    Order toOrder(OrderEntity orderEntity);

    @Mapping(source = "productId", target = "productId")
    @Mapping(source = "quantity", target = "quantity")
    OrderItem toOrderItem(OrderItemDto orderItemDto);


    @Mapping(target = "productId", source = "orderItem.productId")
    @Mapping(target = "quantity", source = "orderItem.quantity")
    @Mapping(target = "order", source = "orderEntity")
    @Mapping(target = "price", expression = "java(0.0)")
    OrderItemEntity toOrderItemEntity(OrderItem orderItem, OrderEntity orderEntity);

    @Mapping(source = "id", target = "orderId")
    @Mapping(source = "transactionId", target = "transactionId")
    PlaceOrderResponseDto toPlaceOrderResponseDto(Order order);

    default List<OrderItemEntity> toOrderItemEntities(List<OrderItem> orderItems, OrderEntity orderEntity) {
        return orderItems.stream()
                .map(orderItem -> toOrderItemEntity(orderItem, orderEntity))
                .collect(Collectors.toList());
    }
}

