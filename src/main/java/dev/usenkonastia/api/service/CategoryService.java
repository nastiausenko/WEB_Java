package dev.usenkonastia.api.service;

import dev.usenkonastia.api.domain.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    Category createCategory(Category category);
    void deleteCategory(UUID categoryId);
    Category getCategoryById(UUID categoryId);
    List<Category> getAllCategories();
}
