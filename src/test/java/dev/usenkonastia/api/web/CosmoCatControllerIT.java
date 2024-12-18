package dev.usenkonastia.api.web;


import com.fasterxml.jackson.databind.ObjectMapper;
import dev.usenkonastia.api.dto.cosmoCat.CosmoCatEntryDto;
import dev.usenkonastia.api.featuretoggle.FeatureToggleExtension;
import dev.usenkonastia.api.featuretoggle.FeatureToggles;
import dev.usenkonastia.api.featuretoggle.annotation.DisabledFeatureToggle;
import dev.usenkonastia.api.featuretoggle.annotation.EnabledFeatureToggle;
import dev.usenkonastia.api.repository.CosmoCatRepository;
import dev.usenkonastia.api.repository.entity.CosmoCatEntity;
import dev.usenkonastia.api.service.CosmoCatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.reset;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(FeatureToggleExtension.class)
@DisplayName("Cosmo Cat Controller Tests")
@Testcontainers
public class CosmoCatControllerIT extends AbstractIt {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CosmoCatRepository cosmoCatRepository;

    @SpyBean
    private CosmoCatService cosmoCatService;

    @BeforeEach
    void setUp() {
        reset(cosmoCatService);
        cosmoCatRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @EnabledFeatureToggle(FeatureToggles.COSMO_CATS)
    void testGetCosmoCats() throws Exception {
        saveCosmoCatEntity();
        buildCosmoCat("Comet", "comet@example.com");
        buildCosmoCat("Star", "star@example.com");
        mockMvc.perform(get("/api/v1/cosmo-cat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cosmoCats.length()").value(3))
                .andExpect(jsonPath("$.cosmoCats[0].catName").value("Luna"))
                .andExpect(jsonPath("$.cosmoCats[1].catName").value("Comet"))
                .andExpect(jsonPath("$.cosmoCats[2].catName").value("Star"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisabledFeatureToggle(FeatureToggles.COSMO_CATS)
    void testDisabledGetCosmoCats() throws Exception {
        saveCosmoCatEntity();
        mockMvc.perform(get("/api/v1/cosmo-cat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void testCreateCat() throws Exception {
        CosmoCatEntryDto newCat = CosmoCatEntryDto.builder()
                .catName("Nebula")
                .email("nebula@example.com")
                .build();

        mockMvc.perform(post("/api/v1/cosmo-cat")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCat)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.catName").value("Nebula"))
                .andExpect(jsonPath("$.email").value("nebula@example.com"));

        assertThat(cosmoCatRepository.findByNaturalId("nebula@example.com")).isPresent();
    }

    @Test
    @WithMockUser
    void testCreateCatFailedValidation() throws Exception {
        CosmoCatEntryDto newCat = CosmoCatEntryDto.builder()
                .catName("Nebula")
                .email("nebula")
                .build();

        mockMvc.perform(post("/api/v1/cosmo-cat")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCat)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.invalidParams[0].fieldName").value("email"))
                .andExpect(jsonPath("$.invalidParams[0].reason").value("Email should be valid"));
    }

    @Test
    @WithMockUser
    void testGetCatById() throws Exception {
        saveCosmoCatEntity();
        mockMvc.perform(get("/api/v1/cosmo-cat/{email}", "luna@email.com")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.catName").value("Luna"))
                .andExpect(jsonPath("$.email").value("luna@email.com"));
    }

    @Test
    @WithMockUser
    void testGetCatByIdNotFound() throws Exception {
        saveCosmoCatEntity();
        mockMvc.perform(get("/api/v1/cosmo-cat/{email}", "any@email.com")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Cat Not Found"));
    }

    @Test
    @WithMockUser
    void testDeleteCat() throws Exception {
        saveCosmoCatEntity();
        mockMvc.perform(delete("/api/v1/cosmo-cat/{email}", "luna@email.com")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    private void buildCosmoCat(String name, String email) {
        cosmoCatRepository.save(CosmoCatEntity.builder()
                .id(UUID.randomUUID())
                .catName(name)
                .email(email)
                .build());
    }

    private void saveCosmoCatEntity() {
        cosmoCatRepository.save(CosmoCatEntity.builder()
                .id(UUID.randomUUID())
                .catName("Luna")
                .email("luna@email.com")
                .build());
    }
}
