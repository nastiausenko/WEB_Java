package dev.usenkonastia.api.service;

import dev.usenkonastia.api.domain.category.Category;

import java.util.List;

public interface CategoryService {
    Category createCategory(Category category);
    Category updateCategory(Long id, Category category);
    Category getCategoryById(Long id);
    List<Category> getAllCategories();
    void deleteCategory(Long id);
}
