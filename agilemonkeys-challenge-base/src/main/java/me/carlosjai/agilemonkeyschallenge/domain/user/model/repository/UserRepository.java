package me.carlosjai.agilemonkeyschallenge.domain.user.model.repository;

import java.util.Optional;
import me.carlosjai.agilemonkeyschallenge.domain.user.model.entity.UserEntity;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long>,
    QuerydslPredicateExecutor<UserEntity> {

  Optional<UserEntity> findByUsernameIgnoreCase(String username);
}
