package dev.usenkonastia.api.service;

import dev.usenkonastia.api.domain.CosmoCat;
import dev.usenkonastia.api.repository.CosmoCatRepository;
import dev.usenkonastia.api.repository.entity.CosmoCatEntity;
import dev.usenkonastia.api.service.exception.CatNotFoundException;
import dev.usenkonastia.api.service.exception.PersistenceException;
import dev.usenkonastia.api.service.impl.CosmoCatServiceImpl;
import dev.usenkonastia.api.service.mapper.CosmoCatMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Cosmo Cat Service Tests")
@SpringBootTest(classes = CosmoCatServiceImpl.class)
public class CosmoCatServiceTest {
    @Autowired
    private CosmoCatService cosmoCatService;
    @MockBean
    private CosmoCatRepository cosmoCatRepository;
    @MockBean
    private CosmoCatMapper cosmoCatMapper;
    private CosmoCat cosmoCat;
    private CosmoCatEntity cosmoCatEntity;

    @BeforeEach
    void setUp() {
        cosmoCat = buildCat("Comet", "comet@email.com");
        cosmoCatEntity = CosmoCatEntity.builder()
                .catName("Comet")
                .email("comet@email.com")
                .build();
    }

    @Test
    void testCreateCat() {
        when(cosmoCatMapper.toCosmoCatEntity((any(CosmoCat.class)))).thenReturn(cosmoCatEntity);
        when(cosmoCatRepository.save(any(CosmoCatEntity.class))).thenReturn(cosmoCatEntity);
        when(cosmoCatMapper.toCosmoCat(any(CosmoCatEntity.class))).thenReturn(cosmoCat);

        CosmoCat newCat = cosmoCatService.createCat(cosmoCat);

        assertThat(newCat).isNotNull();
        assertThat(newCat.getCatName()).isEqualTo("Comet");

        verify(cosmoCatRepository).save(any(CosmoCatEntity.class));
    }

    @Test
    void testCreateCatPersistenceException() {
        when(cosmoCatMapper.toCosmoCatEntity(any(CosmoCat.class))).thenReturn(cosmoCatEntity);
        when(cosmoCatRepository.save(any(CosmoCatEntity.class))).thenThrow(new RuntimeException("Database error"));

        PersistenceException exception = assertThrows(PersistenceException.class,
                () -> cosmoCatService.createCat(cosmoCat));

        assertThat(exception.getCause()).isInstanceOf(RuntimeException.class);
        assertThat(exception.getCause().getMessage()).isEqualTo("Database error");
    }

    @Test
    void testGetCatById() {
        when(cosmoCatRepository.findByNaturalId(any(String.class))).thenReturn(Optional.of(cosmoCatEntity));
        when(cosmoCatMapper.toCosmoCat(any(CosmoCatEntity.class))).thenReturn(cosmoCat);

        CosmoCat result = cosmoCatService.getCatById(cosmoCat.getEmail());
        assertThat(result).isNotNull();
        assertEquals(cosmoCat.getCatId(), result.getCatId());
        assertEquals(cosmoCat.getCatName(), result.getCatName());
        assertEquals(cosmoCat.getEmail(), result.getEmail());
    }

    @Test
    void testGetCatByIdNotFound() {
        assertThrows(CatNotFoundException.class , () -> cosmoCatService.getCatById(String.valueOf(UUID.randomUUID())));
    }

    @Test
    void testDeleteCat() {
        when(cosmoCatRepository.findByNaturalId(any(String.class))).thenReturn(Optional.of(cosmoCatEntity));
        when(cosmoCatMapper.toCosmoCat(any(CosmoCatEntity.class))).thenReturn(cosmoCat);

        cosmoCatService.deleteCat(cosmoCat.getEmail());
        assertThat(cosmoCatService.getCosmoCats()).doesNotContain(cosmoCat);
    }

    @Test
    void testDeleteCatNotFound() {
        assertThrows(PersistenceException.class , () -> cosmoCatService.deleteCat(String.valueOf(UUID.randomUUID())));
    }

    @Test
    void testGetCosmoCats() {
        CosmoCat cosmoCat2 = buildCat("Nebula", "nebula@email.com");
        CosmoCatEntity cosmoCatEntity1 = CosmoCatEntity.builder()
                .catName("Nebula")
                .email("nebula@email.com").build();
        List<CosmoCat> cosmoCats = List.of(cosmoCat, cosmoCat2);
        List<CosmoCatEntity> cosmoCatEntities = List.of(cosmoCatEntity, cosmoCatEntity1);
        when(cosmoCatRepository.findAll()).thenReturn(cosmoCatEntities);
        when(cosmoCatMapper.toCosmoCatList(any())).thenReturn(cosmoCats);

        List<CosmoCat> result = cosmoCatService.getCosmoCats();
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(cosmoCat, cosmoCat2);
    }

    @Test
    void testGetAllCategoriesPersistenceException() {
        when(cosmoCatRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        PersistenceException exception = assertThrows(PersistenceException.class,
                cosmoCatService::getCosmoCats);

        assertThat(exception.getCause()).isInstanceOf(RuntimeException.class);
        assertThat(exception.getCause().getMessage()).isEqualTo("Database error");
    }

    private static CosmoCat buildCat(String name, String email) {
        return CosmoCat.builder()
                .catId(UUID.randomUUID())
                .catName(name)
                .email(email)
                .build();
    }
}
