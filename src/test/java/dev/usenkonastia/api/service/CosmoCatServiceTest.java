package dev.usenkonastia.api.service;

import dev.usenkonastia.api.domain.CosmoCat;
import dev.usenkonastia.api.service.exception.CatNotFoundException;
import dev.usenkonastia.api.service.impl.CosmoCatServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Cosmo Cat Service Tests")
@SpringBootTest(classes = CosmoCatServiceImpl.class)
public class CosmoCatServiceTest {
    @Autowired
    private CosmoCatService cosmoCatService;
    private CosmoCat cosmoCat;

    @BeforeEach
    void setUp() {
        cosmoCat = buildCat("Comet", "comet@email.com");
    }

    @Test
    void testCreateCat() {
        CosmoCat cat = cosmoCatService.createCat(cosmoCat);

        assertThat(cat).isNotNull();
        assertThat(cat.getCatId()).isNotNull();
        assertThat(cosmoCatService.getCosmoCats()).contains(cat);
    }

    @Test
    void testGetCatById() {
        cosmoCat = cosmoCatService.createCat(cosmoCat);
        CosmoCat result = cosmoCatService.getCatById(cosmoCat.getCatId());
        assertThat(result).isNotNull();
        assertEquals(cosmoCat.getCatId(), result.getCatId());
        assertEquals(cosmoCat.getCatName(), result.getCatName());
        assertEquals(cosmoCat.getEmail(), result.getEmail());
    }

    @Test
    void testUpdateCat() {
        cosmoCat = cosmoCatService.createCat(cosmoCat);
        CosmoCat updatedCat = CosmoCat.builder()
                .catId(cosmoCat.getCatId())
                .catName("Super Comet")
                .email("supercomet@email.com")
                .build();

        CosmoCat result = cosmoCatService.updateCat(cosmoCat.getCatId(), updatedCat);

        assertThat(result).isNotNull();
        assertEquals("Super Comet", result.getCatName());
        assertEquals("supercomet@email.com", result.getEmail());
    }

    @Test
    void testUpdateCatNotFound() {
        assertThrows(CatNotFoundException.class , () -> cosmoCatService.updateCat(UUID.randomUUID(), cosmoCat));
    }

    @Test
    void testGetCatByIdNotFound() {
        assertThrows(CatNotFoundException.class , () -> cosmoCatService.getCatById(UUID.randomUUID()));
    }

    @Test
    void testDeleteCat() {
        cosmoCat = cosmoCatService.createCat(cosmoCat);
        cosmoCatService.deleteCat(cosmoCat.getCatId());
        assertThat(cosmoCatService.getCosmoCats()).doesNotContain(cosmoCat);
    }

    @Test
    void testDeleteCatNotFound() {
        assertThrows(CatNotFoundException.class , () -> cosmoCatService.deleteCat(UUID.randomUUID()));
    }

    @ParameterizedTest
    @MethodSource("getCosmoCats")
    void testGetCosmoCats(CosmoCat cosmoCat) {
        cosmoCat = cosmoCatService.createCat(cosmoCat);
        List<CosmoCat> result = cosmoCatService.getCosmoCats();
        assertThat(result).contains(cosmoCat);
    }

    private static Stream<CosmoCat> getCosmoCats() {
        return Stream.of(
                buildCat("Milo", "milo@email.com"),
                buildCat("Luna", "luna@email.com"),
                buildCat("Nova", "nova@emailcom")
        );
    }

    private static CosmoCat buildCat(String name, String email) {
        return CosmoCat.builder()
                .catId(UUID.randomUUID())
                .catName(name)
                .email(email)
                .build();
    }
}
