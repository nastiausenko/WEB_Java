package dev.usenkonastia.api.service;

import dev.usenkonastia.api.domain.Category;
import dev.usenkonastia.api.domain.Product;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    Category createCategory(Category category);
    Category updateCategory(UUID id, Category category);
    Category getCategoryById(UUID id);
    List<Category> getAllCategories();
    void deleteCategory(UUID id);
}
