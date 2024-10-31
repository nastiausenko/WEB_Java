package dev.usenkonastia.api.service.impl;

import dev.usenkonastia.api.domain.Product;
import dev.usenkonastia.api.service.ProductService;
import dev.usenkonastia.api.service.exception.ProductNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final List<Product> products = new ArrayList<>(buildAllProductMock());

    @Override
    public Product createProduct(Product product) {
            Product newProduct = Product.builder()
                    .id(UUID.randomUUID())
                    .categoryId(product.getCategoryId())
                    .productName(product.getProductName())
                    .description(product.getDescription())
                    .price(product.getPrice())
                    .quantity(product.getQuantity())
                    .build();
            products.add(newProduct);
            return newProduct;
    }

    @Override
    public Product getProductById(UUID productId) {
        return Optional.of(products.stream()
                        .filter(details -> details.getId().equals(productId)).findFirst())
                .get()
                .orElseThrow(() -> {
                    log.info("Product with id {} not found in mock", productId);
                    return new ProductNotFoundException(productId);
                });
    }

    @Override
    public Product updateProduct(UUID productId, Product updatedProduct) {
            Product existingProduct = getProductById(productId);

            Product updatedExistingProduct = Product.builder()
                    .id(productId)
                    .categoryId(updatedProduct.getCategoryId())
                    .productName(updatedProduct.getProductName())
                    .description(updatedProduct.getDescription())
                    .price(updatedProduct.getPrice())
                    .quantity(updatedProduct.getQuantity())
                    .build();

            log.info("Product with id {} updated successfully", productId);
            products.set(products.indexOf(existingProduct), updatedExistingProduct);
            return updatedExistingProduct;
    }

    @Override
    public List<Product> getAllProducts() {
        return products;
    }

    @Override
    public void deleteProduct(UUID productId) {
        Product product = getProductById(productId);
        products.remove(product);
        log.info("Product with id {} deleted successfully", productId);
    }


    private List<Product> buildAllProductMock() {
        return List.of(
                Product.builder()
                        .id(UUID.fromString("5cbcebc8-2be7-4fbd-9152-5eac90f13726"))
                        .categoryId(UUID.randomUUID().toString())
                        .productName("Антигравітаційний клубок ниток")
                        .description("Ідеальний для котів, що люблять вишукані іграшки в умовах нульової гравітації.")
                        .price(99.99)
                        .quantity(10)
                        .build(),

                Product.builder()
                        .id(UUID.fromString("0912e7f9-e151-400a-a294-b0dde0ec9010"))
                        .categoryId(UUID.randomUUID().toString())
                        .productName("Космічне молоко")
                        .description("Натуральне молоко від космічних корів, багате на вітаміни та мінерали.")
                        .price(49.99)
                        .quantity(25)
                        .build(),

                Product.builder()
                        .id(UUID.fromString("14f9dd98-eabe-4942-b8be-afd370139a98"))
                        .categoryId(UUID.randomUUID().toString())
                        .productName("Лазерна указка 5000")
                        .description("Улюблена іграшка для котів, що хочуть випробувати свою спритність у космосі.")
                        .price(24.99)
                        .quantity(50)
                        .build(),

                Product.builder()
                        .id(UUID.fromString("6de9b537-3f8f-413a-9c34-9f90a3bd5cf8"))
                        .categoryId(UUID.randomUUID().toString())
                        .productName("Костюм астронавта для кота")
                        .description("Зручний та стильний костюм для ваших космічних подорожей.")
                        .price(199.99)
                        .quantity(5)
                        .build()
        );
    }
}
