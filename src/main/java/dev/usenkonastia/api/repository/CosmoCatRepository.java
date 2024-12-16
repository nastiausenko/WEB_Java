package dev.usenkonastia.api.repository;

import dev.usenkonastia.api.repository.entity.CosmoCatEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface CosmoCatRepository extends NaturalIdRepository<CosmoCatEntity, String> {

}
