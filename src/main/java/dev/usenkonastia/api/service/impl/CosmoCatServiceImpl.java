package dev.usenkonastia.api.service.impl;

import dev.usenkonastia.api.service.CosmoCatService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CosmoCatServiceImpl implements CosmoCatService {
    @Override
    public List<String> getCosmoCats() {
        return List.of("Luna", "Milo", "Stella", "Nova", "Comet");
    }
}
