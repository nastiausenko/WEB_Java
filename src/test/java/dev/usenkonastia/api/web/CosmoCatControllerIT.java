package dev.usenkonastia.api.web;


import com.fasterxml.jackson.databind.ObjectMapper;
import dev.usenkonastia.api.domain.CosmoCat;
import dev.usenkonastia.api.dto.cosmoCat.CosmoCatDto;
import dev.usenkonastia.api.dto.cosmoCat.CosmoCatEntryDto;
import dev.usenkonastia.api.dto.cosmoCat.CosmoCatListDto;
import dev.usenkonastia.api.featuretoggle.FeatureToggleExtension;
import dev.usenkonastia.api.featuretoggle.FeatureToggles;
import dev.usenkonastia.api.featuretoggle.annotation.DisabledFeatureToggle;
import dev.usenkonastia.api.featuretoggle.annotation.EnabledFeatureToggle;
import dev.usenkonastia.api.service.CosmoCatService;
import dev.usenkonastia.api.service.exception.CatNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(FeatureToggleExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Cosmo Cat Controller Tests")
public class CosmoCatControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CosmoCatService cosmoCatService;

    private static final UUID CAT_ID = UUID.randomUUID();
    private final CosmoCatListDto cosmoCatListDto = buildCosmoCatListDto();
    private CosmoCatDto cosmoCatDto;
    private CosmoCat cosmoCat;

    @BeforeEach
    void setUp() {
        cosmoCatDto = buildCosmoCatDto("Luna", "luna@email.com");
        cosmoCat = CosmoCat.builder()
                .catId(CAT_ID)
                .catName("Luna")
                .email("luna@email.com")
                .build();
    }

    @Test
    @EnabledFeatureToggle(FeatureToggles.COSMO_CATS)
    void testGetCosmoCats() throws Exception {
        when(cosmoCatService.getCosmoCats()).thenReturn(buildCatList());

        mockMvc.perform(get("/api/v1/cosmo-cat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cosmoCats.length()").value(cosmoCatListDto.getCosmoCats().size()))
                .andExpect(jsonPath("$.cosmoCats[0].catName").value(cosmoCatListDto.getCosmoCats().get(0).getCatName()))
                .andExpect(jsonPath("$.cosmoCats[1].catName").value(cosmoCatListDto.getCosmoCats().get(1).getCatName()))
                .andExpect(jsonPath("$.cosmoCats[2].catName").value(cosmoCatListDto.getCosmoCats().get(2).getCatName()));
    }

    @Test
    @DisabledFeatureToggle(FeatureToggles.COSMO_CATS)
    void testDisabledGetCosmoCats() throws Exception {
        mockMvc.perform(get("/api/v1/cosmo-cat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cosmoCatDto)))
                .andExpect(status().isNotFound());
    }


    @Test
    void testCreateCat() throws Exception {
        when(cosmoCatService.createCat(any())).thenReturn(cosmoCat);
        mockMvc.perform(post("/api/v1/cosmo-cat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cosmoCatDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.catName").value(cosmoCatDto.getCatName()))
                .andExpect(jsonPath("$.email").value(cosmoCatDto.getEmail()));
    }

    @Test
    void testCreateCatFailedValidation() throws Exception {
        cosmoCatDto = buildCosmoCatDto("Luna", "luna");
        mockMvc.perform(post("/api/v1/cosmo-cat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cosmoCatDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.invalidParams[0].fieldName").value("email"))
                .andExpect(jsonPath("$.invalidParams[0].reason").value("Email should be valid"));
    }

    @Test
    void testGetCatById() throws Exception {
        when(cosmoCatService.getCatById(CAT_ID)).thenReturn(cosmoCat);

        mockMvc.perform(get("/api/v1/cosmo-cat/{id}", CAT_ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cosmoCatDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.catName").value(cosmoCatDto.getCatName()))
                .andExpect(jsonPath("$.email").value(cosmoCatDto.getEmail()));
    }

    @Test
    void testGetCatByIdNotFound() throws Exception {
        when(cosmoCatService.getCatById(any())).thenThrow(CatNotFoundException.class);
        mockMvc.perform(get("/api/v1/cosmo-cat/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Cat Not Found"));
    }


    @Test
    void testUpdateCat() throws Exception {
        CosmoCatDto updatedCatDto = buildCosmoCatDto("Super Space", "usperspace@email.com");
        CosmoCat updatedCat = CosmoCat.builder()
                .catId(CAT_ID)
                .catName(updatedCatDto.getCatName())
                .email(updatedCatDto.getEmail())
                .build();

        when(cosmoCatService.updateCat(any(), any(CosmoCat.class))).thenReturn(updatedCat);

        mockMvc.perform(put("/api/v1/cosmo-cat/{id}", CAT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCatDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.catName").value(updatedCatDto.getCatName()))
                .andExpect(jsonPath("$.email").value(updatedCatDto.getEmail()));
    }

    @Test
    void testUpdateCatNotFound() throws Exception {
        CosmoCatDto updatedCatDto = buildCosmoCatDto("Space", "space@email.com");
        when(cosmoCatService.updateCat(any(), any(CosmoCat.class))).thenThrow(CatNotFoundException.class);

        mockMvc.perform(put("/api/v1/cosmo-cat/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCatDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Cat Not Found"));
    }

    @Test
    void testDeleteCat() throws Exception {
        doNothing().when(cosmoCatService).deleteCat(CAT_ID);

        mockMvc.perform(delete("/api/v1/cosmo-cat/{id}", CAT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteCatNotFound() throws Exception {
        doThrow(CatNotFoundException.class).when(cosmoCatService).deleteCat(any());
        mockMvc.perform(delete("/api/v1/cosmo-cat/{id}", CAT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    private static CosmoCatDto buildCosmoCatDto(String name, String email) {
        return CosmoCatDto.builder()
                .catName(name)
                .email(email)
                .build();
    }


    private static CosmoCatListDto buildCosmoCatListDto() {
        return CosmoCatListDto.builder()
                .cosmoCats(List.of(
                        buildCosmoCatEntryDto("Milo", "milo@email.com"),
                        buildCosmoCatEntryDto("Nova", "nova@email.com"),
                        buildCosmoCatEntryDto("Comet", "comet@email.com")))
                .build();
    }

    private static List<CosmoCat> buildCatList() {
        return buildCosmoCatListDto().getCosmoCats().stream()
                .map(dto -> CosmoCat.builder()
                        .catId(dto.getCatId())
                        .catName(dto.getCatName())
                        .email(dto.getEmail())
                        .build())
                .toList();
    }

    private static CosmoCatEntryDto buildCosmoCatEntryDto(String name, String email) {
        return CosmoCatEntryDto.builder().catId(UUID.randomUUID()).catName(name).email(email).build();
    }
}
