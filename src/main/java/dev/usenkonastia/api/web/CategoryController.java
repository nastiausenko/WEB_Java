package dev.usenkonastia.api.web;

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
@Tag(name = "Category")
@RequiredArgsConstructor
@RequestMapping("/api/v1/category")
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping
    public ResponseEntity<CategoryListDto> getCategories() {
        return ResponseEntity.ok(categoryMapper.toCategoryListDto(categoryService.getAllCategories()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable UUID id) {
        return ResponseEntity.ok(categoryMapper.toCategoryDto(categoryService.getCategoryById(id)));
    }

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        return ResponseEntity.ok(categoryMapper.toCategoryDto(categoryService.createCategory(categoryMapper.toCategory(categoryDto))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
