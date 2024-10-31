package dev.usenkonastia.api.web;

import dev.usenkonastia.api.domain.Category;
import dev.usenkonastia.api.dto.category.CategoryDto;
import dev.usenkonastia.api.dto.category.CategoryListDto;
import dev.usenkonastia.api.service.CategoryService;
import dev.usenkonastia.api.service.mapper.CategoryMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("api/v1/category")
@Tag(name = "Category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable UUID id) {
        return ResponseEntity.ok(categoryMapper.toCategoryDto(categoryService.getCategoryById(id)));
    }

    @GetMapping
    public ResponseEntity<CategoryListDto> getAllCategories() {
        return ResponseEntity.ok(categoryMapper.toCategoryListDto(categoryService.getAllCategories()));
    }

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        Category category = categoryMapper.toCategory(categoryDto);
        return ResponseEntity.ok(categoryMapper.toCategoryDto(categoryService.createCategory(category)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable UUID id, @Valid @RequestBody CategoryDto categoryDto) {
        Category category = categoryMapper.toCategory(categoryDto);
        return ResponseEntity.ok(categoryMapper.toCategoryDto(categoryService.updateCategory(id, category)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
