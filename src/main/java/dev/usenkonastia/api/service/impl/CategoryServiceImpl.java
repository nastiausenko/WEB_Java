package dev.usenkonastia.api.service.impl;

import dev.usenkonastia.api.domain.Category;
import dev.usenkonastia.api.repository.CategoryRepository;
import dev.usenkonastia.api.service.CategoryService;
import dev.usenkonastia.api.service.exception.CategoryNotFoundException;
import dev.usenkonastia.api.service.mapper.CategoryMapper;
import jakarta.persistence.PersistenceException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public Category createCategory(Category category) {
        try {
            return categoryMapper.toCategory(categoryRepository.save(categoryMapper.toCategoryEntity(category)));
        } catch (Exception e) {
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteCategory(UUID categoryId) {
        try {
            categoryRepository.findById(categoryId).orElseThrow(() -> {
                log.info("Category with id {} not found", categoryId);
                return new CategoryNotFoundException(categoryId);
            });
            categoryRepository.deleteById(categoryId);
        } catch (Exception e) {
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Category getCategoryById(UUID categoryId) {
        try {
            return categoryMapper.toCategory(categoryRepository.findById(categoryId).orElseThrow(()-> new CategoryNotFoundException(categoryId)));
        } catch (Exception e) {
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> getAllCategories() {
        try {
            return categoryMapper.toCategoryList(categoryRepository.findAll().iterator());
        } catch (Exception e) {
            throw new PersistenceException(e.getMessage());
        }
    }
}
