package dev.usenkonastia.api.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.usenkonastia.api.dto.order.PlaceOrderRequestDto;
import dev.usenkonastia.api.repository.CosmoCatRepository;
import dev.usenkonastia.api.repository.OrderRepository;
import dev.usenkonastia.api.repository.entity.CosmoCatEntity;
import dev.usenkonastia.api.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@DisplayName("Order Controller Tests")
@Testcontainers
public class OrderControllerIT extends AbstractIt {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CosmoCatRepository cosmoCatRepository;

    @SpyBean
    private OrderService orderService;
    private PlaceOrderRequestDto placeOrderRequestDto;
    private static final String CART_ID = "a5b9bf50-f427-476c-9bec-d4c41c314d29";

    @BeforeEach
    void setUp() {
        reset(orderService);
        orderRepository.deleteAll();
        cosmoCatRepository.deleteAll();
    }

    @Test
    void testPlaceOrder() throws Exception {
        saveCatEntity();
        placeOrderRequestDto = PlaceOrderRequestDto.builder()
                .products(List.of())
                .totalPrice(123.4)
                .build();
        mockMvc.perform(post("/api/v1/sunny@email.com/orders/{cartId}", CART_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(placeOrderRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").isNotEmpty())
                .andExpect(jsonPath("$.transactionId").isNotEmpty());
    }

    @Test
    void testPlaceOrderCatNotFound() throws Exception {
        saveCatEntity();
        placeOrderRequestDto = PlaceOrderRequestDto.builder()
                .products(List.of())
                .totalPrice(123.4)
                .build();
        mockMvc.perform(post("/api/v1/any@email.com/orders/{cartId}", UUID.randomUUID().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(placeOrderRequestDto)))
                .andExpect(status().isNotFound());
    }

    private void saveCatEntity() {
        CosmoCatEntity catEntity = CosmoCatEntity.builder()
                .catName("Sunny")
                .email("sunny@email.com")
                .build();
        cosmoCatRepository.save(catEntity);
    }
}
