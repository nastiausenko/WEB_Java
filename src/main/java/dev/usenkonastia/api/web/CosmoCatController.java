package dev.usenkonastia.api.web;

import dev.usenkonastia.api.featuretoggle.FeatureToggles;
import dev.usenkonastia.api.featuretoggle.annotation.FeatureToggle;
import dev.usenkonastia.api.service.CosmoCatService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "Cosmo Cat")
@RequestMapping("/cosmo-cat")
@RequiredArgsConstructor
public class CosmoCatController {
    private final CosmoCatService cosmoCatService;

    @GetMapping
    @FeatureToggle(FeatureToggles.COSMO_CATS)
    public ResponseEntity<List<String>> getCosmoCats() {
        return ResponseEntity.ok(cosmoCatService.getCosmoCats());
    }
}
