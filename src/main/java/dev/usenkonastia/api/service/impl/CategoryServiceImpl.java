package dev.usenkonastia.api.service.impl;

import dev.usenkonastia.api.domain.Category;
import dev.usenkonastia.api.service.CategoryService;
import dev.usenkonastia.api.service.exception.CategoryNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final List<Category> categories = new ArrayList<>(buildAllCategoryMock());

    @Override
    public Category createCategory(Category category) {
        Category newCategory = Category.builder()
                .id(UUID.randomUUID())
                .name(category.getName())
                .products(category.getProducts())
                .build();
        log.info("Category created: {}", category);
        return newCategory;
    }

    @Override
    public Category updateCategory(UUID id, Category category) {
            Category existingCategory = getCategoryById(id);
            Category updatedExistingCategory = Category.builder()
                    .id(id)
                    .name(category.getName())
                    .products(category.getProducts())
                    .build();
            log.info("Category with id {} updated successfully", id);
            categories.set(categories.indexOf(existingCategory), updatedExistingCategory);
            return updatedExistingCategory;
    }

    @Override
    public Category getCategoryById(UUID id) {
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
    public void deleteCategory(UUID id) {
        Category category = getCategoryById(id);
        categories.remove(category);
        log.info("Category with id {} deleted successfully", id);
    }

    private List<Category> buildAllCategoryMock() {
        return List.of(
                Category.builder()
                        .id(UUID.randomUUID())
                        .name("Galaxy Toys")
                        .products(new ArrayList<>())
                        .build(),
                Category.builder()
                        .id(UUID.randomUUID())
                        .name("Space Food")
                        .products(new ArrayList<>())
                        .build(),
                Category.builder()
                        .id(UUID.randomUUID())
                        .name("Astronaut Suits")
                        .products(new ArrayList<>())
                        .build()
        );
    }
}
