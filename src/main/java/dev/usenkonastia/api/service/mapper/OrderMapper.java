package dev.usenkonastia.api.service.mapper;

import dev.usenkonastia.api.domain.order.Order;
import dev.usenkonastia.api.domain.order.OrderContext;
import dev.usenkonastia.api.domain.order.OrderItem;
import dev.usenkonastia.api.dto.order.OrderItemDto;
import dev.usenkonastia.api.dto.order.PlaceOrderRequestDto;
import dev.usenkonastia.api.dto.order.PlaceOrderResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(source = "cartId", target = "cartId")
    @Mapping(source = "customerReference", target = "customerReference")
    @Mapping(source = "orderDto.products", target = "products")
    @Mapping(source = "orderDto.totalPrice", target = "totalPrice")
    OrderContext toOrderContext(String cartId, String customerReference, PlaceOrderRequestDto orderDto);

    @Mapping(source = "productId", target = "productId")
    @Mapping(source = "quantity", target = "quantity")
    OrderItem toOrderItem(OrderItemDto orderItemDto);

    @Mapping(source = "id", target = "orderId")
    @Mapping(source = "transactionId", target = "transactionId")
    PlaceOrderResponseDto toPlaceOrderResponseDto(Order order);
}
