package dev.usenkonastia.api.service.impl;

import dev.usenkonastia.api.domain.Product;
import dev.usenkonastia.api.repository.CategoryRepository;
import dev.usenkonastia.api.repository.ProductRepository;
import dev.usenkonastia.api.repository.entity.CategoryEntity;
import dev.usenkonastia.api.repository.entity.ProductEntity;
import dev.usenkonastia.api.repository.projection.ProductReportProjection;
import dev.usenkonastia.api.service.ProductService;
import dev.usenkonastia.api.service.exception.CategoryNotFoundException;
import dev.usenkonastia.api.service.exception.PersistenceException;
import dev.usenkonastia.api.service.exception.ProductNotFoundException;
import dev.usenkonastia.api.service.mapper.ProductMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional(propagation = Propagation.NESTED)
    public Product createProduct(Product product) {
        try {
            if (product.getCategoryId() != null) {
                categoryRepository.findById(UUID.fromString(product.getCategoryId()))
                        .orElseThrow(() -> new CategoryNotFoundException(UUID.fromString(product.getCategoryId())));
            }
            return productMapper.toProduct(productRepository.save(productMapper.toProductEntity(product)));
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Product getProductById(UUID productId) {
        ProductEntity product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId));
        return productMapper.toProduct(product);
    }

    @Override
    @Transactional
    public Product updateProduct(UUID productId, Product updatedProduct) {
            ProductEntity product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId));
            try {
                product.setId(productId);
                product.setProductName(updatedProduct.getProductName());
                product.setDescription(updatedProduct.getDescription());
                product.setPrice(updatedProduct.getPrice());
                product.setQuantity(updatedProduct.getQuantity());

            if (updatedProduct.getCategoryId() != null) {
                UUID categoryId = UUID.fromString(updatedProduct.getCategoryId());
                CategoryEntity categoryEntity = categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new CategoryNotFoundException(categoryId));
                product.setCategory(categoryEntity);
            }

            return productMapper.toProduct(productRepository.save(product));
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
    }


    @Override
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        try {
            return productMapper.toProductList(productRepository.findAll().iterator());
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    @Transactional
    public void deleteProduct(UUID productId) {
        try {
            productRepository.deleteById(productId);
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductReportProjection> getTopSellingProducts() {
        try {
            return productRepository.findTopSellingProducts();
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
    }
}
