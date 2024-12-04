package dev.usenkonastia.api.service;

import dev.usenkonastia.api.config.MappersTestConfiguration;
import dev.usenkonastia.api.domain.Category;
import dev.usenkonastia.api.domain.Product;
import dev.usenkonastia.api.repository.CategoryRepository;
import dev.usenkonastia.api.repository.entity.CategoryEntity;
import dev.usenkonastia.api.service.exception.CategoryNotFoundException;
import dev.usenkonastia.api.service.exception.PersistenceException;
import dev.usenkonastia.api.service.impl.CategoryServiceImpl;
import dev.usenkonastia.api.service.mapper.CategoryMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Category Service Tests")
@SpringBootTest(classes = CategoryServiceImpl.class)
@Import(MappersTestConfiguration.class)
public class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private CategoryMapper categoryMapper;

    private Category category;
    private CategoryEntity categoryEntity;

    @BeforeEach
    void setUp() {
        category = buildCategory("Space Food", List.of());
        categoryEntity = CategoryEntity.builder()
                .categoryName("Space Food")
                .products(List.of())
                .build();
    }

    @Test
    void testCreateCategory() {
        when(categoryMapper.toCategoryEntity(any(Category.class))).thenReturn(categoryEntity);
        when(categoryRepository.save(any(CategoryEntity.class))).thenReturn(categoryEntity);
        when(categoryMapper.toCategory(any(CategoryEntity.class))).thenReturn(category);

        Category newCategory = categoryService.createCategory(category);

        assertThat(newCategory).isNotNull();
        assertThat(newCategory.getName()).isEqualTo("Space Food");

        verify(categoryRepository).save(any(CategoryEntity.class));
    }

    @Test
    void testCreateCategoryPersistenceException() {
        when(categoryMapper.toCategoryEntity(any(Category.class))).thenReturn(categoryEntity);
        when(categoryRepository.save(any(CategoryEntity.class))).thenThrow(new RuntimeException("Database error"));

        PersistenceException exception = assertThrows(PersistenceException.class,
                () -> categoryService.createCategory(category));

        assertThat(exception.getCause()).isInstanceOf(RuntimeException.class);
        assertThat(exception.getCause().getMessage()).isEqualTo("Database error");
    }


    @Test
    void testGetCategoryById() {
        when(categoryRepository.findById(any(UUID.class))).thenReturn(Optional.of(categoryEntity));
        when(categoryMapper.toCategory(any(CategoryEntity.class))).thenReturn(category);
        Category result = categoryService.getCategoryById(category.getId());

        assertThat(result).isNotNull();
        assertEquals(category.getId(), result.getId());
        assertEquals(category.getName(), result.getName());
        assertEquals(category.getProducts().size(), result.getProducts().size());
    }

    @Test
    void testGetCategoryByIdNotFound() {
        assertThrows(CategoryNotFoundException.class, () -> categoryService.getCategoryById(UUID.randomUUID()));
    }

    @Test
    void testDeleteCategory() {
        when(categoryRepository.findById(any(UUID.class))).thenReturn(Optional.of(categoryEntity));
        when(categoryMapper.toCategory(any(CategoryEntity.class))).thenReturn(category);

        categoryService.deleteCategory(category.getId());
        assertThat(categoryService.getAllCategories()).doesNotContain(category);
    }

    @Test
    void testDeleteCategoryNotFound() {
        assertThrows(PersistenceException.class, () -> categoryService.deleteCategory(UUID.randomUUID()));
    }

    @Test
    void testGetAllCategories() {
        CategoryEntity categoryEntity2 = CategoryEntity.builder()
                .id(UUID.randomUUID())
                .categoryName("Astronaut Clothes")
                .build();
        Category category2 = buildCategory("Astronaut Clothes", List.of());
        List<CategoryEntity> categoryEntities = List.of(categoryEntity, categoryEntity2);
        List<Category> categories = List.of(category, category2);

        when(categoryRepository.findAll()).thenReturn(categoryEntities);
        when(categoryMapper.toCategoryList(any())).thenReturn(categories);

        List<Category> result = categoryService.getAllCategories();

        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(category, category2);
    }

    @Test
    void testGetAllCategoriesPersistenceException() {
        when(categoryRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        PersistenceException exception = assertThrows(PersistenceException.class,
                categoryService::getAllCategories);

        assertThat(exception.getCause()).isInstanceOf(RuntimeException.class);
        assertThat(exception.getCause().getMessage()).isEqualTo("Database error");
    }


    private static Category buildCategory(String name, List<Product> products) {
        return Category.builder()
                .id(UUID.randomUUID())
                .name(name)
                .products(products)
                .build();
    }
}
