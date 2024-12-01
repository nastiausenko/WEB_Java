package dev.usenkonastia.api.web;

import dev.usenkonastia.api.domain.CosmoCat;
import dev.usenkonastia.api.dto.cosmoCat.CosmoCatDto;
import dev.usenkonastia.api.dto.cosmoCat.CosmoCatListDto;
import dev.usenkonastia.api.featuretoggle.FeatureToggles;
import dev.usenkonastia.api.featuretoggle.annotation.FeatureToggle;
import dev.usenkonastia.api.service.CosmoCatService;
import dev.usenkonastia.api.service.mapper.CosmoCatMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Cosmo Cat")
@RequestMapping("/api/v1/cosmo-cat")
@RequiredArgsConstructor
public class CosmoCatController {
    private final CosmoCatService cosmoCatService;
    private final CosmoCatMapper cosmoCatMapper;

    @GetMapping
    @FeatureToggle(FeatureToggles.COSMO_CATS)
    public ResponseEntity<CosmoCatListDto> getCosmoCats() {
        return ResponseEntity.ok(cosmoCatMapper.toCosmoCatListDto(cosmoCatService.getCosmoCats()));
    }

    @GetMapping("/{email}")
    public ResponseEntity<CosmoCatDto> getCatById(@PathVariable String email) {
        return ResponseEntity.ok(cosmoCatMapper.toCosmoCatDto(cosmoCatService.getCatById(email)));
    }

    @PostMapping
    public ResponseEntity<CosmoCatDto> createCat(@Valid @RequestBody CosmoCatDto cosmoCatDto) {
        CosmoCat cat = cosmoCatMapper.toCosmoCat(cosmoCatDto);
        return ResponseEntity.ok(cosmoCatMapper.toCosmoCatDto(cosmoCatService.createCat(cat)));
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<?> deleteCat(@PathVariable String email) {
        cosmoCatService.deleteCat(email);
        return ResponseEntity.noContent().build();
    }
}
