package me.carlosjai.agilemonkeyschallenge.domain.customer.model.repository;


import java.util.Optional;
import me.carlosjai.agilemonkeyschallenge.domain.customer.model.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long>,
    QuerydslPredicateExecutor<CustomerEntity> {

  Optional<CustomerEntity> findByEmailIgnoreCase(String email);
}
