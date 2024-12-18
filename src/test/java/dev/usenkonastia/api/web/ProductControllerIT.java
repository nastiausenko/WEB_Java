package dev.usenkonastia.api.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.usenkonastia.api.dto.product.ProductDto;
import dev.usenkonastia.api.featuretoggle.FeatureToggleExtension;
import dev.usenkonastia.api.featuretoggle.FeatureToggles;
import dev.usenkonastia.api.featuretoggle.annotation.DisabledFeatureToggle;
import dev.usenkonastia.api.featuretoggle.annotation.EnabledFeatureToggle;
import dev.usenkonastia.api.repository.CategoryRepository;
import dev.usenkonastia.api.repository.ProductRepository;
import dev.usenkonastia.api.repository.entity.CategoryEntity;
import dev.usenkonastia.api.repository.entity.ProductEntity;
import dev.usenkonastia.api.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(FeatureToggleExtension.class)
@DisplayName("Product Controller Tests")
@Testcontainers
public class ProductControllerIT extends AbstractIt {
    private UUID savedProductId;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;
    private UUID categoryId;

    @BeforeEach
    void setUp() {
        reset(productService);
        productRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    @EnabledFeatureToggle(FeatureToggles.KITTY_PRODUCTS)
    @WithMockUser(roles = "ADMIN")
    void testCreateProduct() throws Exception {
        saveProductEntity();
        ProductDto productDto = ProductDto.builder()
                        .productName("Space Lazer")
                        .categoryId(categoryId.toString())
                        .productDescription("Lazer for astronauts")
                        .price(12.7)
                        .quantity(2)
                        .build();
        mockMvc.perform(post("/api/v1/product")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value(productDto.getProductName()))
                .andExpect(jsonPath("$.productDescription").value(productDto.getProductDescription()))
                .andExpect(jsonPath("$.price").value(productDto.getPrice()))
                .andExpect(jsonPath("$.quantity").value(productDto.getQuantity()));

        assertThat(productRepository.findByProductName(productDto.getProductName())).isNotNull();
    }

    @ParameterizedTest
    @EnabledFeatureToggle(FeatureToggles.KITTY_PRODUCTS)
    @MethodSource("provideInvalidProductDtos")
    @WithMockUser(roles = "ADMIN")
    void testCreateProductFailedValidation(ProductDto productDto, String fieldName, String message) throws Exception {
        mockMvc.perform(post("/api/v1/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.invalidParams[0].fieldName").value(fieldName))
                .andExpect(jsonPath("$.invalidParams[0].reason").value(message));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisabledFeatureToggle(FeatureToggles.KITTY_PRODUCTS)
    void testDisabledCreateProduct() throws Exception {
        ProductDto productDto = ProductDto.builder()
                .productName("Space Lazer")
                .productDescription("Lazer for astronauts")
                .price(12.7)
                .quantity(2)
                .build();

        mockMvc.perform(post("/api/v1/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetProductById() throws Exception {
        saveProductEntity();
        mockMvc.perform(get("/api/v1/product/{id}", savedProductId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("Space Milk"))
                .andExpect(jsonPath("$.productDescription").value("Space Milk for astronauts"))
                .andExpect(jsonPath("$.price").value(123.5))
                .andExpect(jsonPath("$.quantity").value(12));
    }

    @Test
    void testGetProductByIdNotFound() throws Exception {
        saveProductEntity();
        mockMvc.perform(get("/api/v1/product/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Product Not Found"));
    }

    @Test
    void testGetAllProducts() throws Exception {
        saveProductEntity();
        mockMvc.perform(get("/api/v1/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products[0].id").value(savedProductId.toString()))
                .andExpect(jsonPath("$.products.length()").value(1));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateProduct() throws Exception {
        saveProductEntity();
        ProductDto updatedProductDto = buildProductDto("Updated Space Milk", "Updated description", 39.9, 50);

        mockMvc.perform(put("/api/v1/product/{id}", savedProductId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProductDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value(updatedProductDto.getProductName()))
                .andExpect(jsonPath("$.productDescription").value(updatedProductDto.getProductDescription()))
                .andExpect(jsonPath("$.price").value(updatedProductDto.getPrice()))
                .andExpect(jsonPath("$.quantity").value(updatedProductDto.getQuantity()));
    }

    @ParameterizedTest
    @WithMockUser(roles = "ADMIN")
    @MethodSource("provideInvalidProductDtos")
    void testUpdateProductFailedValidation(ProductDto productDto, String fieldName, String message) throws Exception {
        saveProductEntity();
        mockMvc.perform(put("/api/v1/product/{id}", savedProductId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.invalidParams[0].fieldName").value(fieldName))
                .andExpect(jsonPath("$.invalidParams[0].reason").value(message));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateProductNotFound() throws Exception {
        ProductDto updatedProductDto = buildProductDto("Space Product", "This product does not exist", 39.9, 50);

        mockMvc.perform(put("/api/v1/product/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProductDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Product Not Found"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteProduct() throws Exception {
        saveProductEntity();
        mockMvc.perform(delete("/api/v1/product/{id}", savedProductId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    private static Stream<Arguments> provideInvalidProductDtos() {
        return Stream.of(
                Arguments.of(buildProductDto("Milk", "Milk for astronauts", 29.7, 32), "productName",
                        "Name must contain a cosmic term (e.g., 'star', 'galaxy', 'comet')"),
                Arguments.of(buildProductDto("Space Milk", "Milk for astronauts", -29.7, 32), "price",
                        "Price must be positive"),
                Arguments.of(buildProductDto("Space Milk", "Milk for astronauts", 29.7, -1), "quantity",
                        "Quantity must be positive"),
                Arguments.of(buildProductDto(null, "Milk for astronauts", 29.7, 32), "productName",
                        "Product name cannot be null")
        );
    }

    private static ProductDto buildProductDto(String name, String description, double price, int quantity) {
        return ProductDto.builder()
                .productName(name)
                .productDescription(description)
                .price(price)
                .quantity(quantity)
                .build();
    }

    private void saveProductEntity() {
        CategoryEntity category = categoryRepository.save(CategoryEntity.builder()
                .categoryName("Galaxy Food").build());
        ProductEntity productEntity = productRepository.save(ProductEntity.builder()
                .productName("Space Milk")
                .description("Space Milk for astronauts")
                .category(category)
                .quantity(12)
                .price(123.5)
                .build());
        savedProductId = productEntity.getId();
        categoryId = category.getId();
    }
}
