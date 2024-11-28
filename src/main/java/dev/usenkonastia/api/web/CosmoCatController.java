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

import java.util.UUID;

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

    @GetMapping("/{id}")
    public ResponseEntity<CosmoCatDto> getCatById(@PathVariable UUID id) {
        return ResponseEntity.ok(cosmoCatMapper.toCosmoCatDto(cosmoCatService.getCatById(id)));
    }

    @PostMapping
    public ResponseEntity<CosmoCatDto> createCat(@Valid @RequestBody CosmoCatDto cosmoCatDto) {
        CosmoCat cat = cosmoCatMapper.toCosmoCat(cosmoCatDto);
        return ResponseEntity.ok(cosmoCatMapper.toCosmoCatDto(cosmoCatService.createCat(cat)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CosmoCatDto> updateCat(@PathVariable UUID id, @Valid @RequestBody CosmoCatDto cosmoCatDto) {
        CosmoCat cat = cosmoCatMapper.toCosmoCat(cosmoCatDto);
        return ResponseEntity.ok(cosmoCatMapper.toCosmoCatDto(cosmoCatService.updateCat(id, cat)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCat(@PathVariable UUID id) {
        cosmoCatService.deleteCat(id);
        return ResponseEntity.noContent().build();
    }
}
