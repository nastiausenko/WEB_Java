package dev.usenkonastia.api.service;

import dev.usenkonastia.api.domain.CosmoCat;

import java.util.List;

public interface CosmoCatService {
    CosmoCat createCat(CosmoCat cat);
    CosmoCat getCatById(String email);
    List<CosmoCat> getCosmoCats();
    void deleteCat(String email);
}
