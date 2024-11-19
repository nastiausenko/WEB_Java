package dev.usenkonastia.api.web;


import com.fasterxml.jackson.databind.ObjectMapper;
import dev.usenkonastia.api.featuretoggle.FeatureToggleExtension;
import dev.usenkonastia.api.featuretoggle.FeatureToggles;
import dev.usenkonastia.api.featuretoggle.annotation.DisabledFeatureToggle;
import dev.usenkonastia.api.featuretoggle.annotation.EnabledFeatureToggle;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(FeatureToggleExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Cosmo Cat Controller Tests")
public class CosmoCatControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @EnabledFeatureToggle(FeatureToggles.COSMO_CATS)
    void testEnabledGetCosmoCats() throws Exception {
        mockMvc.perform(get("/api/v1/cosmo-cat")).andExpect(status().isOk());
    }

    @Test
    @DisabledFeatureToggle(FeatureToggles.COSMO_CATS)
    void testDisabledGetCosmoCats() throws Exception {
        mockMvc.perform(get("/api/v1/cosmo-cat")).andExpect(status().isNotFound());
    }
}
