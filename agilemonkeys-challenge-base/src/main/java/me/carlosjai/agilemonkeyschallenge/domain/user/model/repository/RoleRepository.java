package me.carlosjai.agilemonkeyschallenge.domain.user.model.repository;

import me.carlosjai.agilemonkeyschallenge.domain.user.model.entity.RoleEntity;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<RoleEntity, Long> {

  RoleEntity findByNameIgnoreCase(String name);
}
