package dev.usenkonastia.api.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.usenkonastia.api.dto.category.CategoryDto;
import dev.usenkonastia.api.repository.CategoryRepository;
import dev.usenkonastia.api.repository.entity.CategoryEntity;
import dev.usenkonastia.api.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@DisplayName("Category Controller Tests")
@Testcontainers
public class CategoryControllerIT extends AbstractIt {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryService categoryService;

    private UUID categoryId;

    @BeforeEach
    void setUp() {
        reset(categoryService);
        categoryRepository.deleteAll();
    }

    @Test
    void testGetCategories() throws Exception {
        saveCategoryEntity();
        buildCategory("Astronaut Clothes");
        buildCategory("Galaxy Toys");
        mockMvc.perform(get("/api/v1/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categories.length()").value(3))
                .andExpect(jsonPath("$.categories[0].name").value("Space Food"))
                .andExpect(jsonPath("$.categories[1].name").value("Astronaut Clothes"))
                .andExpect(jsonPath("$.categories[2].name").value("Galaxy Toys"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateCategory() throws Exception {
        CategoryDto newCategory = CategoryDto.builder()
                .categoryName("Earth Vehicles")
                .build();

        mockMvc.perform(post("/api/v1/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCategory)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryName").value("Earth Vehicles"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateCategoryFailedValidation() throws Exception {
        CategoryDto newCategory = CategoryDto.builder()
                .categoryName("Furniture")
                .build();

        mockMvc.perform(post("/api/v1/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCategory)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.invalidParams[0].fieldName").value("categoryName"))
                .andExpect(jsonPath("$.invalidParams[0].reason").value("Name must contain a cosmic term (e.g., 'star', 'galaxy', 'comet')"));
    }

    @Test
    void testGetCategoryById() throws Exception {
        saveCategoryEntity();
        mockMvc.perform(get("/api/v1/category/{id}", categoryId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryName").value("Space Food"));
    }

    @Test
    void testGetCategoryByIdNotFound() throws Exception {
        saveCategoryEntity();
        mockMvc.perform(get("/api/v1/category/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Category Not Found"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteCategory() throws Exception {
        saveCategoryEntity();
        mockMvc.perform(delete("/api/v1/category/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteCategoryNotFound() throws Exception {
        saveCategoryEntity();
        mockMvc.perform(delete("/api/v1/category/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    private void buildCategory(String name) {
        categoryRepository.save(CategoryEntity.builder()
                .id(UUID.randomUUID())
                .categoryName(name)
                .products(List.of())
                .build());
    }

    private void saveCategoryEntity() {
        CategoryEntity categoryEntity = categoryRepository.save(CategoryEntity.builder()
                .id(UUID.randomUUID())
                .categoryName("Space Food")
                .products(List.of())
                .build());
        categoryId = categoryEntity.getId();
    }
}
