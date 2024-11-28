package dev.usenkonastia.api.web;

import dev.usenkonastia.api.domain.Product;
import dev.usenkonastia.api.dto.product.ProductDto;
import dev.usenkonastia.api.dto.product.ProductListDto;
import dev.usenkonastia.api.featuretoggle.FeatureToggles;
import dev.usenkonastia.api.featuretoggle.annotation.FeatureToggle;
import dev.usenkonastia.api.service.ProductService;
import dev.usenkonastia.api.service.mapper.ProductMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@Tag(name = "Product")
@Validated
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final ProductMapper productMapper;

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable UUID id) {
        return ResponseEntity.ok(productMapper.toProductDto(productService.getProductById(id)));
    }

    @GetMapping
    public ResponseEntity<ProductListDto> getAllProducts() {
        return ResponseEntity.ok(productMapper.toProductListDto(productService.getAllProducts()));
    }

    @PostMapping
    @FeatureToggle(FeatureToggles.KITTY_PRODUCTS)
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto productDto) {
        Product product = productMapper.toProduct(productDto);
        return ResponseEntity.ok(productMapper.toProductDto(productService.createProduct(product)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable UUID id, @Valid @RequestBody ProductDto productDto) {
        Product product = productMapper.toProduct(productDto);
        return ResponseEntity.ok(productMapper.toProductDto(productService.updateProduct(id, product)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
