package dev.usenkonastia.api.web;

import dev.usenkonastia.api.domain.category.Category;
import dev.usenkonastia.api.dto.category.CategoryDto;
import dev.usenkonastia.api.dto.category.CategoryListDto;
import dev.usenkonastia.api.service.CategoryService;
import dev.usenkonastia.api.service.mapper.CategoryMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id) {
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
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryDto categoryDto) {
        Category category = categoryMapper.toCategory(categoryDto);
        return ResponseEntity.ok(categoryMapper.toCategoryDto(categoryService.updateCategory(id, category)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
