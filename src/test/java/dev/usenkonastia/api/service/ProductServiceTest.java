package dev.usenkonastia.api.service;

import dev.usenkonastia.api.domain.Product;
import dev.usenkonastia.api.repository.CategoryRepository;
import dev.usenkonastia.api.repository.ProductRepository;
import dev.usenkonastia.api.repository.entity.ProductEntity;
import dev.usenkonastia.api.repository.projection.ProductReportProjection;
import dev.usenkonastia.api.service.exception.PersistenceException;
import dev.usenkonastia.api.service.exception.ProductNotFoundException;
import dev.usenkonastia.api.service.impl.ProductServiceImpl;
import dev.usenkonastia.api.service.mapper.ProductMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@DisplayName("Product Service Tests")
@SpringBootTest(classes = ProductServiceImpl.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductServiceTest {
    @Autowired
    private ProductService productService;

    @MockBean
    private ProductRepository productRepository;
    @MockBean
    private CategoryRepository categoryRepository;
    @MockBean
    private ProductMapper productMapper;

    private ProductReportProjection projection1;
    private ProductReportProjection projection2;

    private Product product;
    private ProductEntity productEntity;

    @BeforeEach
    void setUp() {
        product = buildProduct("Venus Yarn",
                "Soft yarn for space cats", 48.8, 36 );
        productEntity = ProductEntity.builder()
                .productName("Venus Yarn")
                .description("Soft yarn for space cats")
                .price(48.8)
                .quantity(36)
                .build();

        projection1 = mock(ProductReportProjection.class);

        projection2 = mock(ProductReportProjection.class);
    }

    @Test
    void testCreateProduct() {
        when(productMapper.toProductEntity(any(Product.class))).thenReturn(productEntity);
        when(productRepository.save(any(ProductEntity.class))).thenReturn(productEntity);
        when(productMapper.toProduct(any(ProductEntity.class))).thenReturn(product);

        Product newProduct = productService.createProduct(product);

        assertThat(newProduct).isNotNull();
        assertThat(newProduct.getProductName()).isEqualTo("Venus Yarn");
        verify(productRepository).save(any(ProductEntity.class));
    }

    @Test
    void testCreateProductCategoryNotFound() {
        product = Product.builder()
                .categoryId(UUID.randomUUID().toString())
                .productName("Venus Yarn")
                .description("Soft yarn for space cats")
                .price(48.8)
                .quantity(36)
                .build();
        when(productRepository.findById(any(UUID.class))).thenReturn(Optional.of(productEntity));
        when(categoryRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        assertThrows(PersistenceException.class, () -> productService.createProduct(product));
    }

    @Test
    void testCreateProductPersistenceException() {
        when(productMapper.toProductEntity(any(Product.class))).thenReturn(productEntity);
        when(productRepository.save(any(ProductEntity.class))).thenThrow(new RuntimeException("Database error"));

        PersistenceException exception = assertThrows(PersistenceException.class,
                () -> productService.createProduct(product));

        assertThat(exception.getCause()).isInstanceOf(RuntimeException.class);
        assertThat(exception.getCause().getMessage()).isEqualTo("Database error");
    }

    @Test
    void testGetProductById() {
        when(productRepository.findById(any(UUID.class))).thenReturn(Optional.of(productEntity));
        when(productMapper.toProduct(any(ProductEntity.class))).thenReturn(product);

        Product result = productService.getProductById(product.getId());

        assertThat(result).isNotNull();
        assertEquals(product.getId(), result.getId());
        assertEquals(product.getProductName(), result.getProductName());
        assertEquals(product.getDescription(), result.getDescription());
        assertEquals(product.getPrice(), result.getPrice());
        assertEquals(product.getQuantity(), result.getQuantity());
    }

    @Test
    void testUpdateProduct() {
        ProductEntity updatedProductEntity = ProductEntity.builder()
                .id(product.getId())
                .productName("Updated Comet Laser")
                .description("Updated laser toy description")
                .price(35.0)
                .quantity(8)
                .build();
        Product updatedProduct = Product.builder()
                .id(product.getId())
                .productName("Updated Comet Laser")
                .description("Updated laser toy description")
                .price(35.0)
                .quantity(8)
                .build();
        when(productRepository.findById(any(UUID.class))).thenReturn(Optional.of(productEntity));
        when(productRepository.save(any(ProductEntity.class))).thenReturn(updatedProductEntity);
        when(productMapper.toProduct(any(ProductEntity.class))).thenReturn(updatedProduct);

        Product result = productService.updateProduct(product.getId(), updatedProduct);

        assertThat(result).isNotNull();
        assertEquals("Updated Comet Laser", result.getProductName());
        assertEquals("Updated laser toy description", result.getDescription());
        assertEquals(35.0, result.getPrice());
        assertEquals(8, result.getQuantity());
    }

    @Test
    void testUpdateProductCategoryNotFound() {
        Product updatedProduct = Product.builder()
                .id(product.getId())
                .categoryId(UUID.randomUUID().toString())
                .productName("Updated Comet Laser")
                .description("Updated laser toy description")
                .price(35.0)
                .quantity(8)
                .build();
        when(productRepository.findById(any(UUID.class))).thenReturn(Optional.of(productEntity));
        when(categoryRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        assertThrows(PersistenceException.class, () -> productService.updateProduct(product.getId(), updatedProduct));
    }

    @Test
    void testUpdateProductNotFound() {
        assertThrows(ProductNotFoundException.class , () -> productService.updateProduct(UUID.randomUUID(), product));
    }

    @Test
    void testGetProductByIdNotFound() {
        assertThrows(ProductNotFoundException.class , () -> productService.getProductById(UUID.randomUUID()));
    }

    @Test
    void testDeleteProduct() {
        when(productRepository.findById(any(UUID.class))).thenReturn(Optional.of(productEntity));
        when(productMapper.toProduct(any(ProductEntity.class))).thenReturn(product);

        productService.deleteProduct(product.getId());
        assertThat(productService.getAllProducts()).doesNotContain(product);
    }

    @Test
    void testDeleteProductNotFound() {
        assertThrows(PersistenceException.class , () -> productService.deleteProduct(UUID.randomUUID()));
    }

    @Test
    void testGetAllProducts() {
        Product product2 = buildProduct("Galaxy Cake", "Cake for space cats", 23.6, 76);
        ProductEntity productEntity2 = ProductEntity.builder()
                .productName("Galaxy Cake")
                .description("Cake for space cats")
                .price(23.6)
                .quantity(76)
                .build();
        List<Product> products = List.of(product, product2);
        List<ProductEntity> productEntities = List.of(productEntity, productEntity2);

        when(productRepository.findAll()).thenReturn(productEntities);
        when(productMapper.toProductList(any())).thenReturn(products);

        List<Product> result = productService.getAllProducts();

        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(product, product2);
    }

    @Test
    void testGetTopSellingProducts() {
        List<ProductReportProjection> projections = List.of(projection1, projection2);
        when(productRepository.findTopSellingProducts()).thenReturn(projections);

        List<ProductReportProjection> result = productService.getTopSellingProducts();

        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(projection1, projection2);

        verify(productRepository, times(1)).findTopSellingProducts();
    }

    private static Product buildProduct(String name, String description, double price, int quantity) {
        return Product.builder()
                .id(UUID.randomUUID())
                .productName(name)
                .description(description)
                .price(price)
                .quantity(quantity)
                .build();
    }
}
