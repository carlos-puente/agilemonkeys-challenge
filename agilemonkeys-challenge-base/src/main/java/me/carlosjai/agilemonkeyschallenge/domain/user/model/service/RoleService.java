package me.carlosjai.agilemonkeyschallenge.domain.user.model.service;

import lombok.AllArgsConstructor;
import me.carlosjai.agilemonkeyschallenge.domain.user.model.entity.RoleEntity;
import me.carlosjai.agilemonkeyschallenge.domain.user.model.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class RoleService {

  private RoleRepository repository;

  public RoleEntity findByName(String name) {
    return repository.findByNameIgnoreCase(name);
  }

}
