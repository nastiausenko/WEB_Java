package dev.usenkonastia.api.service.impl;

import dev.usenkonastia.api.domain.category.Category;
import dev.usenkonastia.api.service.CategoryService;
import dev.usenkonastia.api.service.exception.CategoryNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final List<Category> categories = new ArrayList<>(buildAllCategoryMock());

    @Override
    public Category createCategory(Category category) {
        categories.add(category);
        log.info("Category created: {}", category);
        return category;
    }

    @Override
    public Category updateCategory(Long id, Category category) {
        Category existingCategory = getCategoryById(id);
        existingCategory = Category.builder()
                .id(existingCategory.getId())
                .name(category.getName() != null ? category.getName() : existingCategory.getName())
                .products(category.getProducts() != null ? category.getProducts() : existingCategory.getProducts())
                .build();
        log.info("Category with id {} updated successfully", id);
        return existingCategory;
    }

    @Override
    public Category getCategoryById(Long id) {
        return Optional.of(categories.stream()
                        .filter(details -> details.getId().equals(id)).findFirst())
                .get()
                .orElseThrow(() -> {
                    log.info("Category with id {} not found in mock", id);
                    return new CategoryNotFoundException(id);
                });
    }

    @Override
    public List<Category> getAllCategories() {
        return categories;
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = getCategoryById(id);
        categories.remove(category);
        log.info("Category with id {} deleted successfully", id);
    }

    private List<Category> buildAllCategoryMock() {
        return List.of(
                Category.builder()
                        .id(1L)
                        .name("Космічні іграшки")
                        .products(new ArrayList<>())
                        .build(),
                Category.builder()
                        .id(2L)
                        .name("Космічна їжа")
                        .products(new ArrayList<>())
                        .build(),
                Category.builder()
                        .id(3L)
                        .name("Космічні костюми")
                        .products(new ArrayList<>())
                        .build()
        );
    }
}
