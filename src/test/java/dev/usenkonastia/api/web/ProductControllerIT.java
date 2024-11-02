package dev.usenkonastia.api.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.usenkonastia.api.domain.Product;
import dev.usenkonastia.api.dto.product.ProductDto;
import dev.usenkonastia.api.dto.product.ProductEntryDto;
import dev.usenkonastia.api.dto.product.ProductListDto;
import dev.usenkonastia.api.service.ProductService;
import dev.usenkonastia.api.service.exception.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Product Controller Tests")
public class ProductControllerIT {
    private static final UUID PRODUCT_ID = UUID.randomUUID();
    private final ProductListDto productListDto = buildProductListDto();
    private ProductDto productDto;
    private Product mockProduct;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productDto = buildProductDto("Space Milk",
                "Milk for astronauts", 29.7, 32);
        mockProduct = Product.builder()
                .id(PRODUCT_ID)
                .productName("Space Milk")
                .description("Milk for astronauts")
                .categoryId(productDto.getCategoryId())
                .price(29.7)
                .quantity(32)
                .build();
    }

    @Test
    void testCreateProduct() throws Exception {
        when(productService.createProduct(any())).thenReturn(mockProduct);
        mockMvc.perform(post("/api/v1/product")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value(productDto.getProductName()))
                .andExpect(jsonPath("$.productDescription").value(productDto.getProductDescription()))
                .andExpect(jsonPath("$.price").value(productDto.getPrice()))
                .andExpect(jsonPath("$.quantity").value(productDto.getQuantity()));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidProductDtos")
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
    void testGetProductById() throws Exception {
        when(productService.getProductById(PRODUCT_ID)).thenReturn(mockProduct);

        mockMvc.perform(get("/api/v1/product/{id}", PRODUCT_ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value(productDto.getProductName()))
                .andExpect(jsonPath("$.productDescription").value(productDto.getProductDescription()))
                .andExpect(jsonPath("$.price").value(productDto.getPrice()))
                .andExpect(jsonPath("$.quantity").value(productDto.getQuantity()));
    }

    @Test
    void testGetProductByIdNotFound() throws Exception {
        when(productService.getProductById(any())).thenThrow(ProductNotFoundException.class);
        mockMvc.perform(get("/api/v1/product/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Product Not Found"));
    }

    @Test
    void testGetAllProducts() throws Exception {
        when(productService.getAllProducts()).thenReturn(buildProductList());

        mockMvc.perform(get("/api/v1/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products.length()").value(productListDto.getProducts().size()))
                .andExpect(jsonPath("$.products[0].productName").value(productListDto.getProducts().get(0).getProductName()))
                .andExpect(jsonPath("$.products[1].productName").value(productListDto.getProducts().get(1).getProductName()))
                .andExpect(jsonPath("$.products[2].productName").value(productListDto.getProducts().get(2).getProductName()));
    }

    @Test
    void testUpdateProduct() throws Exception {
        ProductDto updatedProductDto = buildProductDto("Updated Space Milk", "Updated description", 39.9, 50);
        Product updatedProduct = Product.builder()
                .id(PRODUCT_ID)
                .productName(updatedProductDto.getProductName())
                .description(updatedProductDto.getProductDescription())
                .categoryId(updatedProductDto.getCategoryId())
                .price(updatedProductDto.getPrice())
                .quantity(updatedProductDto.getQuantity())
                .build();

        when(productService.updateProduct(any(), any(Product.class))).thenReturn(updatedProduct);

        mockMvc.perform(put("/api/v1/product/{id}", PRODUCT_ID)
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
    @MethodSource("provideInvalidProductDtos")
    void testUpdateProductFailedValidation(ProductDto productDto, String fieldName, String message) throws Exception {
        mockMvc.perform(put("/api/v1/product/{id}", PRODUCT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.invalidParams[0].fieldName").value(fieldName))
                .andExpect(jsonPath("$.invalidParams[0].reason").value(message));
    }

    @Test
    void testUpdateProductNotFound() throws Exception {
        ProductDto updatedProductDto = buildProductDto("Space Product", "This product does not exist", 39.9, 50);
        when(productService.updateProduct(any(), any(Product.class))).thenThrow(ProductNotFoundException.class);

        mockMvc.perform(put("/api/v1/product/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProductDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Product Not Found"));
    }

    @Test
    void testDeleteProduct() throws Exception {
        doNothing().when(productService).deleteProduct(PRODUCT_ID);

        mockMvc.perform(delete("/api/v1/product/{id}", PRODUCT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteProductNotFound() throws Exception {
        doThrow(ProductNotFoundException.class).when(productService).deleteProduct(any());
        mockMvc.perform(delete("/api/v1/product/{id}", PRODUCT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    private static Stream<Arguments> provideInvalidProductDtos() {
        return Stream.of(
                Arguments.of(buildProductDto("Milk", "Milk for astronauts", 29.7, 32), "productName",
                        "Product name must contain a cosmic term (e.g., 'star', 'galaxy', 'comet')"),
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
                .categoryId(UUID.randomUUID().toString())
                .productDescription(description)
                .price(price)
                .quantity(quantity)
                .build();
    }


    private static ProductListDto buildProductListDto() {
        return ProductListDto.builder()
                .products(List.of(
                buildProductEntryDto("Space Milk", "Milk for astronauts", 29.7, 32),
                buildProductEntryDto("Venus Yarn", "Soft yarn for cats", 57.0, 445),
                buildProductEntryDto("Sun Laser 3000", "Laser for space cats", 54.2, 435)))
                .build();
    }

    private static List<Product> buildProductList() {
        return buildProductListDto().getProducts().stream()
                .map(dto -> Product.builder()
                        .id(dto.getId())
                        .productName(dto.getProductName())
                        .description(dto.getProductDescription())
                        .categoryId(dto.getCategoryId())
                        .price(dto.getPrice())
                        .quantity(dto.getQuantity())
                        .build())
                .toList();
    }


    private static ProductEntryDto buildProductEntryDto(String name, String description, double price, int quantity) {
        return ProductEntryDto.builder().id(UUID.randomUUID()).productName(name).categoryId(UUID.randomUUID().toString())
                .productDescription(description).price(price).quantity(quantity).build();
    }
}
