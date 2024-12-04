package dev.usenkonastia.api.service.mapper;

import dev.usenkonastia.api.domain.Product;
import dev.usenkonastia.api.dto.product.ProductDto;
import dev.usenkonastia.api.dto.product.ProductEntryDto;
import dev.usenkonastia.api.dto.product.ProductListDto;
import dev.usenkonastia.api.repository.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "productName", target = "productName")
    @Mapping(source = "categoryId", target = "categoryId")
    @Mapping(source = "description", target = "productDescription")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "quantity", target = "quantity")
    ProductDto toProductDto(Product product);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "productName", target = "productName")
    @Mapping(source = "categoryId", target = "categoryId")
    @Mapping(source = "productDescription", target = "description")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "quantity", target = "quantity")
    Product toProduct(ProductDto productDto);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "category.id", target = "categoryId")
    Product toProduct(ProductEntity productEntity);

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

    @Mapping(source = "id", target = "id")
    ProductEntity toProductEntity(Product product);

    default List<Product> toProductList(Iterator<ProductEntity> productEntityIterator) {
        List<Product> result = new ArrayList<>();
        productEntityIterator.forEachRemaining(
                (productEntity) -> result.add(toProduct(productEntity)));
        return result;
    }
}
