package dev.usenkonastia.api.repository;

import dev.usenkonastia.api.repository.entity.OrderEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface OrderRepository extends CrudRepository<OrderEntity, UUID> {

}