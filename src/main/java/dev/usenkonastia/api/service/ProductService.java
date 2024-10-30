package dev.usenkonastia.api.service;


import dev.usenkonastia.api.domain.product.Product;

import java.util.List;

public interface ProductService {
    Product createProduct(Product product);
    Product getProductById(Long productId);
    Product updateProduct(Long productId, Product product);
    List<Product> getAllProducts();
    void deleteProduct(Long productId);
}
