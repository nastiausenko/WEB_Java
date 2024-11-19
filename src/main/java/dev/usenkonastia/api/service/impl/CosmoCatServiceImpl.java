package dev.usenkonastia.api.service.impl;

import dev.usenkonastia.api.domain.CosmoCat;
import dev.usenkonastia.api.service.CosmoCatService;
import dev.usenkonastia.api.service.exception.CatNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class CosmoCatServiceImpl implements CosmoCatService {
    private final List<CosmoCat> cats = new ArrayList<>(buildAllCatMock());

    @Override
    public CosmoCat createCat(CosmoCat cat) {
        cat = CosmoCat.builder()
                .catId(UUID.randomUUID())
                .catName(cat.getCatName())
                .email(cat.getEmail())
                .build();
        cats.add(cat);
        return cat;
    }

    @Override
    public CosmoCat getCatById(UUID catId) {
        return Optional.of(cats.stream()
                        .filter(details -> details.getCatId().equals(catId)).findFirst())
                .get()
                .orElseThrow(() -> {
                    log.info("Cat with id {} not found in mock", catId);
                    return new CatNotFoundException(catId);
                });
    }

    @Override
    public CosmoCat updateCat(UUID catId, CosmoCat updatedCat) {
        CosmoCat existingCat = getCatById(catId);

        CosmoCat updatedExistingCat = CosmoCat.builder()
                .catId(updatedCat.getCatId())
                .catName(updatedCat.getCatName())
                .email(updatedCat.getEmail())
                .build();

        log.info("Cat with id {} updated successfully", catId);
        cats.set(cats.indexOf(existingCat), updatedExistingCat);
        return updatedExistingCat;
    }

    @Override
    public List<CosmoCat> getCosmoCats() {
        return cats;
    }

    @Override
    public void deleteCat(UUID catId) {
        CosmoCat cat = getCatById(catId);
        cats.remove(cat);
        log.info("Cat with id {} deleted successfully", catId);
    }


    private List<CosmoCat> buildAllCatMock() {
        return List.of(
                CosmoCat.builder()
                        .catId(UUID.fromString("5cbcebc8-2be7-4fbd-9152-5eac90f13728"))
                        .catName("Milo")
                        .email("milo@email.com")
                        .build(),

                CosmoCat.builder()
                        .catId(UUID.fromString("5cbcebc8-2be7-4fbd-9152-5eac90f13725"))
                        .catName("Luna")
                        .email("luna@email.com")
                        .build(),

                CosmoCat.builder()
                        .catId(UUID.fromString("5cbcebc8-2be7-4fbd-9152-5eac90f13787"))
                        .catName("Nova")
                        .email("nova@gmail.com")
                        .build()
        );
    }
}
