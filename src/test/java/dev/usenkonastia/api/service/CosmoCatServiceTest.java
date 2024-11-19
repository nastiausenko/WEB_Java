package dev.usenkonastia.api.service;

import dev.usenkonastia.api.service.impl.CosmoCatServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Cosmo Cat Service Tests")
@SpringBootTest(classes = CosmoCatServiceImpl.class)
public class CosmoCatServiceTest {
    @Autowired
    private CosmoCatService cosmoCatService;

    @Test
    void testGetCosmoCats() {
        List<String> cosmoCats = cosmoCatService.getCosmoCats();
        assertThat(cosmoCats)
                .isNotNull()
                .isNotEmpty()
                .containsExactly("Luna", "Milo", "Stella", "Nova", "Comet");
    }
}
