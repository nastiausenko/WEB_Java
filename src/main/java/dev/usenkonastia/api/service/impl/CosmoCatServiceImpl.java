package dev.usenkonastia.api.service.impl;

import dev.usenkonastia.api.domain.CosmoCat;
import dev.usenkonastia.api.repository.CosmoCatRepository;
import dev.usenkonastia.api.repository.entity.CosmoCatEntity;
import dev.usenkonastia.api.service.CosmoCatService;
import dev.usenkonastia.api.service.exception.CatNotFoundException;
import dev.usenkonastia.api.service.exception.PersistenceException;
import dev.usenkonastia.api.service.mapper.CosmoCatMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


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
            throw new PersistenceException(e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CosmoCat getCatById(String email) {
        CosmoCatEntity cosmoCat = cosmoCatRepository.findByNaturalId(email).orElseThrow(() -> new CatNotFoundException(email));
        return cosmoCatMapper.toCosmoCat(cosmoCat);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CosmoCat> getCosmoCats() {
        try {
            return cosmoCatMapper.toCosmoCatList(cosmoCatRepository.findAll().iterator());
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    @Transactional
    public void deleteCat(String email) {
       try {
           cosmoCatRepository.findByNaturalId(email).orElseThrow(() -> new CatNotFoundException(email));
           cosmoCatRepository.deleteByNaturalId(email);
       } catch (Exception e) {
           throw new PersistenceException(e);
       }
    }
}
