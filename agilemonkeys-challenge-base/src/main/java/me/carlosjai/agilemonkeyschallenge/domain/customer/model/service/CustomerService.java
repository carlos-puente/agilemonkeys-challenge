package me.carlosjai.agilemonkeyschallenge.domain.customer.model.service;


import com.querydsl.core.types.Predicate;
import java.util.Optional;
import lombok.AllArgsConstructor;
import me.carlosjai.agilemonkeyschallenge.domain.customer.model.entity.CustomerEntity;
import me.carlosjai.agilemonkeyschallenge.domain.customer.model.repository.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class CustomerService {

  private CustomerRepository repository;

  public Optional<CustomerEntity> getById(final Long customerId) {
    return repository.findById(customerId);
  }

  public Page<CustomerEntity> findAllBy(Predicate predicate, Pageable pageable) {
    return repository.findAll(predicate, pageable);
  }

  @Transactional(readOnly = false)
  public void delete(CustomerEntity entity) {
    repository.delete(entity);
  }

  @Transactional(readOnly = false)
  public CustomerEntity save(final CustomerEntity entity) {
    try {
      return repository.save(entity);
    } catch (Exception ex) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }
  }

  public Optional<CustomerEntity> findByEmail(String email) {
    return repository.findByEmailIgnoreCase(email);
  }
}
