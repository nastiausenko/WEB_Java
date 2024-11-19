package dev.usenkonastia.api.service;

import dev.usenkonastia.api.domain.Product;
import dev.usenkonastia.api.service.exception.ProductNotFoundException;
import dev.usenkonastia.api.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(MockitoExtension.class)
@DisplayName("Product Service Tests")
@SpringBootTest(classes = ProductServiceImpl.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

class ProductServiceTest {
    @Autowired
    private ProductService productService;
    private Product product;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .id(UUID.randomUUID())
                .categoryId(UUID.randomUUID().toString())
                .productName("Venus Yarn")
                .description("Soft yarn for space cats")
                .price(48.8)
                .quantity(36)
                .build();
    }

    @Test
    void testCreateProduct() {
        Product newProduct = productService.createProduct(product);

        assertThat(newProduct).isNotNull();
        assertThat(newProduct.getId()).isNotNull();
        assertThat(productService.getAllProducts()).contains(newProduct);
    }

    @Test
    void testUpdateProduct() {
        productService.createProduct(product);
        Product updatedProduct = Product.builder()
                .id(product.getId())
                .categoryId(product.getCategoryId())
                .productName("Updated Comet Laser")
                .description("Updated laser toy description")
                .price(35.0)
                .quantity(8)
                .build();

        Product result = productService.updateProduct(product.getId(), updatedProduct);

        assertThat(result).isNotNull();
        assertEquals("Updated Comet Laser", result.getProductName());
        assertEquals("Updated laser toy description", result.getDescription());
        assertEquals(35.0, result.getPrice());
        assertEquals(8, result.getQuantity());
    }

    @Test
    void testUpdateProductNotFound() {
        assertThrows(ProductNotFoundException.class , () -> productService.updateProduct(UUID.randomUUID(), product));
    }

    @Test
    void testGetProductById() {
        productService.createProduct(product);
        Product result = productService.getProductById(product.getId());
        assertThat(result).isNotNull();
        assertEquals(product.getId(), result.getId());
        assertEquals(product.getCategoryId(), result.getCategoryId());
        assertEquals(product.getProductName(), result.getProductName());
        assertEquals(product.getDescription(), result.getDescription());
        assertEquals(product.getPrice(), result.getPrice());
        assertEquals(product.getQuantity(), result.getQuantity());
    }

    @Test
    void testGetProductByIdNotFound() {
        assertThrows(ProductNotFoundException.class , () -> productService.getProductById(UUID.randomUUID()));
    }

    @Test
    void testDeleteProduct() {
        productService.createProduct(product);
        productService.deleteProduct(product.getId());
        assertThat(productService.getAllProducts()).doesNotContain(product);
    }

    @Test
    void testDeleteProductNotFound() {
        assertThrows(ProductNotFoundException.class , () -> productService.deleteProduct(UUID.randomUUID()));
    }

    @ParameterizedTest
    @MethodSource("getProducts")
    void testGetAllProducts(Product product) {
        productService.createProduct(product);
        List<Product> result = productService.getAllProducts();
        assertThat(result).contains(product);
    }

    private static Stream<Product> getProducts() {
        return Stream.of(
                buildProduct(UUID.randomUUID().toString(), "Space Cat Suit", "Suit for space exploration", 120.0, 5),
                buildProduct(UUID.randomUUID().toString(), "Galaxy Yarn", "Yarn for crafting in zero gravity", 15.5, 10),
                buildProduct(UUID.randomUUID().toString(), "Sun Laser Toy", "A laser toy for your intergalactic cat", 75.0, 3)
        );
    }

    private static Product buildProduct(String categoryId, String name, String description, double price, int quantity) {
        return Product.builder()
                .id(UUID.randomUUID())
                .categoryId(categoryId)
                .productName(name)
                .description(description)
                .price(price)
                .quantity(quantity)
                .build();
    }
}
