package dev.usenkonastia.api.web;

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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.UUID;

import static dev.usenkonastia.api.util.SecurityUtil.API_KEY_HEADER;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.reset;

import static org.mockito.Mockito.when;
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

    @MockBean
    private JwtDecoder jwtDecoder;

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

        Jwt mockJwt = Jwt.withTokenValue("dummy-token")
                .header("alg", "none")
                .claim("sub", "user@example.com")
                .build();
        when(jwtDecoder.decode(anyString())).thenReturn(mockJwt);
    }

    @Test
    @WithMockUser
    void testPlaceOrder() throws Exception {
        saveCatEntity();
        placeOrderRequestDto = PlaceOrderRequestDto.builder()
                .products(List.of())
                .totalPrice(123.4)
                .build();
        mockMvc.perform(post("/api/v1/orders/sunny@email.com/{cartId}", CART_ID)
                        .header(API_KEY_HEADER, "Bearer dummy-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(placeOrderRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").isNotEmpty())
                .andExpect(jsonPath("$.transactionId").isNotEmpty());
    }

    @Test
    @WithMockUser
    void testPlaceOrderCatNotFound() throws Exception {
        saveCatEntity();
        placeOrderRequestDto = PlaceOrderRequestDto.builder()
                .products(List.of())
                .totalPrice(123.4)
                .build();
        mockMvc.perform(post("/api/v1/orders/any@email.com//{cartId}", UUID.randomUUID().toString())
                        .header(API_KEY_HEADER, "Bearer dummy-token")
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
