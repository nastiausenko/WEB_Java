package dev.usenkonastia.api.service.mapper;

import dev.usenkonastia.api.domain.category.Category;
import dev.usenkonastia.api.dto.category.CategoryDto;
import dev.usenkonastia.api.dto.category.CategoryEntryDto;
import dev.usenkonastia.api.dto.category.CategoryListDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = ProductMapper.class)
public interface CategoryMapper {

    @Mapping(source = "name", target = "categoryName")
    @Mapping(source = "products", target = "products")
    CategoryDto toCategoryDto(Category category);

    default CategoryListDto toCategoryListDto(List<Category> categories) {
        return CategoryListDto.builder().categories(toCategoryEntryDto(categories)).build();
    }

    List<CategoryEntryDto> toCategoryEntryDto(List<Category> categories);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "products", target = "products")
    CategoryEntryDto toCategoryEntryDto(Category category);


    @Mapping(source = "categoryName", target = "name")
    @Mapping(source = "products", target = "products")
    Category toCategory(CategoryDto categoryDto);
}
