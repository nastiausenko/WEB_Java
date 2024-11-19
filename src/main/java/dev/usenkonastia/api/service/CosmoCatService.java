package dev.usenkonastia.api.service;

import dev.usenkonastia.api.domain.CosmoCat;

import java.util.List;
import java.util.UUID;

public interface CosmoCatService {
    CosmoCat createCat(CosmoCat cat);
    CosmoCat getCatById(UUID catId);
    CosmoCat updateCat(UUID catId, CosmoCat cosmoCat);
    List<CosmoCat> getCosmoCats();
    void deleteCat(UUID catId);
}
