package dev.usenkonastia.api.service.mapper;

import dev.usenkonastia.api.domain.Product;
import dev.usenkonastia.api.dto.product.ProductDto;
import dev.usenkonastia.api.dto.product.ProductEntryDto;
import dev.usenkonastia.api.dto.product.ProductListDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "productName", target = "productName")
    @Mapping(source = "categoryId", target = "categoryId")
    @Mapping(source = "description", target = "productDescription")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "quantity", target = "quantity")
    ProductDto toProductDto(Product product);

    @Mapping(source = "productName", target = "productName")
    @Mapping(source = "categoryId", target = "categoryId")
    @Mapping(source = "productDescription", target = "description")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "quantity", target = "quantity")
    Product toProduct(ProductDto productDto);

    default ProductListDto toProductListDto(List<Product> products) {
        return ProductListDto.builder().products(toProductEntryDto(products)).build();
    }

    List<ProductEntryDto> toProductEntryDto(List<Product> products);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "categoryId", target = "categoryId")
    @Mapping(source = "productName", target = "productName")
    @Mapping(source = "description", target = "productDescription")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "quantity", target = "quantity")
    ProductEntryDto toProductEntryDto(Product product);

}
