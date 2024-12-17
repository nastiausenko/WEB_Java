package dev.usenkonastia.api.web;

import dev.usenkonastia.api.domain.order.OrderContext;
import dev.usenkonastia.api.dto.order.PlaceOrderRequestDto;
import dev.usenkonastia.api.dto.order.PlaceOrderResponseDto;
import dev.usenkonastia.api.service.OrderService;
import dev.usenkonastia.api.service.mapper.OrderMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@Validated
@Tag(name = "Order")
@RequestMapping("/api/v1/orders/{customerReference}")
@RequiredArgsConstructor
public class OrderController {
    private final OrderMapper orderMapper;
    private final OrderService orderService;

    @PostMapping("/{cartId}")
    public ResponseEntity<PlaceOrderResponseDto> placeOrder(
        @PathVariable("customerReference") String customerReference,
        @PathVariable("cartId") String cartId,
        @RequestBody @Valid PlaceOrderRequestDto placeOrderDto) {
        log.info("Placing the order for cart with id : {}", cartId);
        OrderContext context = orderMapper.toOrderContext(cartId, customerReference, placeOrderDto);
        return ResponseEntity.ok(orderMapper.toPlaceOrderResponseDto(orderService.placeOrder(context)));
    }
}

