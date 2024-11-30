package dev.usenkonastia.api.service.impl;

import dev.usenkonastia.api.domain.CosmoCat;
import dev.usenkonastia.api.dto.cosmoCat.CosmoCatDto;
import dev.usenkonastia.api.repository.CosmoCatRepository;
import dev.usenkonastia.api.repository.entity.CosmoCatEntity;
import dev.usenkonastia.api.service.CosmoCatService;
import dev.usenkonastia.api.service.exception.CatNotFoundException;
import dev.usenkonastia.api.service.mapper.CosmoCatMapper;
import jakarta.persistence.PersistenceException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class CosmoCatServiceImpl implements CosmoCatService {
    private final CosmoCatRepository cosmoCatRepository;
    private final CosmoCatMapper cosmoCatMapper;

    @Override
    @Transactional(propagation = Propagation.NESTED)
    public CosmoCat createCat(CosmoCat cat) {
        try {
            return cosmoCatMapper.toCosmoCat(cosmoCatRepository.save(cosmoCatMapper.toCosmoCatEntity(cat)));
        } catch (Exception e) {
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CosmoCat getCatById(UUID catId) {
        CosmoCatEntity cosmoCat = cosmoCatRepository.findById(catId).orElseThrow(() -> new CatNotFoundException(catId));
        return cosmoCatMapper.toCosmoCat(cosmoCat);
    }

    @Override
    @Transactional
    public CosmoCat updateCat(UUID catId, CosmoCat updatedCat) {
        CosmoCatEntity cosmoCat = cosmoCatRepository.findById(catId).orElseThrow(() -> new CatNotFoundException(catId));
        try {
            cosmoCat.setId(catId);
            cosmoCat.setCatName(updatedCat.getCatName());
            cosmoCat.setEmail(updatedCat.getEmail());
            return cosmoCatMapper.toCosmoCat(cosmoCatRepository.save(cosmoCat));
        } catch (Exception e) {
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CosmoCat> getCosmoCats() {
        try {
            return cosmoCatMapper.toCosmoCatList(cosmoCatRepository.findAll().iterator());
        } catch (Exception e) {
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteCat(UUID catId) {
       try {
           cosmoCatRepository.deleteById(catId);
       } catch (Exception e) {
           throw new PersistenceException(e.getMessage());
       }
    }
}
