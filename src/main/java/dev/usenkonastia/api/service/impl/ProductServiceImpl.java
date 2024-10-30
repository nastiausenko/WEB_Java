package dev.usenkonastia.api.service.impl;

import dev.usenkonastia.api.domain.product.Product;
import dev.usenkonastia.api.service.ProductService;
import dev.usenkonastia.api.service.exception.ProductNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final List<Product> products = new ArrayList<>(buildAllProductMock());

    @Override
    public Product createProduct(Product product) {
        return Product.builder()
                .categoryId(product.getCategoryId())
                .productName(product.getProductName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .build();
    }

    @Override
    public Product getProductById(Long productId) {
        return Optional.of(products.stream()
                        .filter(details -> details.getId().equals(productId)).findFirst())
                .get()
                .orElseThrow(() -> {
                    log.info("Product with id {} not found in mock", productId);
                    return new ProductNotFoundException(productId);
                });
    }

    @Override
    public Product updateProduct(Long productId, Product updatedProduct) {
        Product existingProduct = products.stream()
                .filter(details -> details.getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> {
                    log.info("Product with id {} not found in mock", productId);
                    return new ProductNotFoundException(productId);
                });

        log.info("Product with id {} updated successfully", productId);
        products.set(products.indexOf(existingProduct), updatedProduct);

        return Product.builder()
                .categoryId(updatedProduct.getCategoryId() != null ? updatedProduct.getCategoryId() : existingProduct.getCategoryId())
                .productName(updatedProduct.getProductName() != null ? updatedProduct.getProductName() : existingProduct.getProductName())
                .description(updatedProduct.getDescription() != null ? updatedProduct.getDescription() : existingProduct.getDescription())
                .price(updatedProduct.getPrice() != null ? updatedProduct.getPrice() : existingProduct.getPrice())
                .quantity(updatedProduct.getQuantity() >= 0 ? updatedProduct.getQuantity() : existingProduct.getQuantity())
                .build();
    }

    @Override
    public List<Product> getAllProducts() {
        return products;
    }

    @Override
    public void deleteProduct(Long productId) {
        Product product = products.stream()
                .filter(details -> details.getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> {
                    log.info("Product with id {} not found in mock", productId);
                    return new ProductNotFoundException(productId);
                });
        products.remove(product);
        log.info("Product with id {} deleted successfully", productId);
    }


    private List<Product> buildAllProductMock() {
        return List.of(
                Product.builder()
                        .id(1L)
                        .categoryId("anti-gravity-yarn")
                        .productName("Антигравітаційний клубок ниток")
                        .description("Ідеальний для котів, що люблять вишукані іграшки в умовах нульової гравітації.")
                        .price(99.99)
                        .quantity(10)
                        .build(),

                Product.builder()
                        .id(2L)
                        .categoryId("space-milk")
                        .productName("Космічне молоко")
                        .description("Натуральне молоко від космічних корів, багате на вітаміни та мінерали.")
                        .price(49.99)
                        .quantity(25)
                        .build(),

                Product.builder()
                        .id(3L)
                        .categoryId("laser-pointer")
                        .productName("Лазерна указка 5000")
                        .description("Улюблена іграшка для котів, що хочуть випробувати свою спритність у космосі.")
                        .price(24.99)
                        .quantity(50)
                        .build(),

                Product.builder()
                        .id(4L)
                        .categoryId("spacesuit")
                        .productName("Костюм астронавта для кота")
                        .description("Зручний та стильний костюм для ваших космічних подорожей.")
                        .price(199.99)
                        .quantity(5)
                        .build()
        );
    }
}
