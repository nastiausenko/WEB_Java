package dev.usenkonastia.api.repository;

import dev.usenkonastia.api.repository.entity.CosmoCatEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CosmoCatRepository extends CrudRepository<CosmoCatEntity, UUID> {

}
