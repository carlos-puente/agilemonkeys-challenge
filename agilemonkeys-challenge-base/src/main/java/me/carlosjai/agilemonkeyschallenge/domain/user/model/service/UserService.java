package me.carlosjai.agilemonkeyschallenge.domain.user.model.service;

import com.querydsl.core.types.Predicate;
import java.util.Optional;
import lombok.AllArgsConstructor;
import me.carlosjai.agilemonkeyschallenge.domain.user.model.entity.UserEntity;
import me.carlosjai.agilemonkeyschallenge.domain.user.model.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class UserService {

  private UserRepository repository;


  public Page<UserEntity> findAllBy(Predicate predicate, Pageable pageable) {
    return repository.findAll(predicate, pageable);
  }

  public Optional<UserEntity> findByUsername(String username) {
    return repository.findByUsernameIgnoreCase(username);
  }

  @Transactional(readOnly = false)
  public UserEntity save(UserEntity newEntity) {
    return repository.save(newEntity);
  }

  public Optional<UserEntity> getUserById(Long userId) {
    return repository.findById(userId);
  }

  @Transactional(readOnly = false)
  public void delete(UserEntity userEntity) {
    repository.delete(userEntity);
  }

}
