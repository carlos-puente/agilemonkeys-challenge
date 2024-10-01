package me.carlosjai.agilemonkeyschallenge.domain.customer.model.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.querydsl.core.types.Predicate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import me.carlosjai.agilemonkeyschallenge.domain.customer.model.entity.CustomerEntity;
import me.carlosjai.agilemonkeyschallenge.domain.customer.model.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class CustomerServiceTest {

  @Mock
  private CustomerRepository repository;

  @InjectMocks
  private CustomerService customerService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGetById_CustomerExists() {
    Long customerId = 1L;
    CustomerEntity customer = new CustomerEntity();
    customer.setSk(customerId);
    when(repository.findById(customerId)).thenReturn(Optional.of(customer));
    Optional<CustomerEntity> result = customerService.getById(customerId);
    assertTrue(result.isPresent());
    assertEquals(customerId, result.get().getSk());
    verify(repository, times(1)).findById(customerId);
  }

  @Test
  void testGetById_CustomerDoesNotExist() {
    Long customerId = 1L;
    when(repository.findById(customerId)).thenReturn(Optional.empty());
    Optional<CustomerEntity> result = customerService.getById(customerId);
    assertFalse(result.isPresent());
    verify(repository, times(1)).findById(customerId);
  }

  @Test
  void testFindAllBy_WithResults() {
    // Arrange
    Predicate predicate = mock(Predicate.class);
    Pageable pageable = PageRequest.of(0, 10);
    CustomerEntity customer = new CustomerEntity();
    customer.setSk(1L);
    List<CustomerEntity> customerList = Collections.singletonList(customer);
    Page<CustomerEntity> customerPage = new PageImpl<>(customerList, pageable, customerList.size());

    when(repository.findAll(predicate, pageable)).thenReturn(customerPage);

    // Act
    Page<CustomerEntity> result = customerService.findAllBy(predicate, pageable);

    // Assert
    assertNotNull(result);
    assertEquals(1, result.getTotalElements());
    assertEquals(1L, result.getContent().get(0).getSk());
    verify(repository, times(1)).findAll(predicate, pageable);
  }

  @Test
  void testFindAllBy_NoResults() {
    // Arrange
    Predicate predicate = mock(Predicate.class);
    Pageable pageable = PageRequest.of(0, 10);
    Page<CustomerEntity> emptyPage = Page.empty(pageable);

    when(repository.findAll(predicate, pageable)).thenReturn(emptyPage);

    // Act
    Page<CustomerEntity> result = customerService.findAllBy(predicate, pageable);

    // Assert
    assertNotNull(result);
    assertTrue(result.isEmpty());
    verify(repository, times(1)).findAll(predicate, pageable);
  }

  @Test
  void testDelete_ValidEntity() {
    // Arrange
    CustomerEntity customer = new CustomerEntity();
    customer.setSk(1L);

    // Act
    customerService.delete(customer);

    // Assert
    verify(repository, times(1)).delete(customer);
  }

  @Test
  void testDelete_RepositoryThrowsException() {
    // Arrange
    CustomerEntity customer = new CustomerEntity();
    customer.setSk(1L);

    doThrow(new RuntimeException("Database error")).when(repository).delete(customer);

    // Act & Assert
    RuntimeException exception = assertThrows(RuntimeException.class,
        () -> customerService.delete(customer));
    assertEquals("Database error", exception.getMessage());
    verify(repository, times(1)).delete(customer);
  }

  @Test
  void testSave_ValidEntity() {
    // Arrange
    CustomerEntity customer = new CustomerEntity();
    customer.setSk(1L);
    customer.setEmail("test@example.com");

    when(repository.save(customer)).thenReturn(customer);

    // Act
    CustomerEntity result = customerService.save(customer);

    // Assert
    assertNotNull(result);
    assertEquals(1L, result.getSk());
    assertEquals("test@example.com", result.getEmail());
    verify(repository, times(1)).save(customer);
  }


  @Test
  void testSave_RepositoryThrowsException() {
    // Arrange
    CustomerEntity customer = new CustomerEntity();
    customer.setSk(1L);

    when(repository.save(customer)).thenThrow(new RuntimeException("Database error"));

    // Act & Assert
    RuntimeException exception = assertThrows(RuntimeException.class,
        () -> customerService.save(customer));
    assertTrue(exception.getMessage().contains("Database error"));

    verify(repository, times(1)).save(customer);
  }

  // Test for findByEmailIgnoreCase method
  @Test
  void testFindByEmail_ValidEmail() {
    // Arrange
    String email = "test@example.com";
    CustomerEntity customer = new CustomerEntity();
    customer.setEmail(email);

    when(repository.findByEmailIgnoreCase(email)).thenReturn(Optional.of(customer));

    // Act
    Optional<CustomerEntity> result = customerService.findByEmail(email);

    // Assert
    assertTrue(result.isPresent());
    assertEquals(email, result.get().getEmail());
    verify(repository, times(1)).findByEmailIgnoreCase(email);
  }

  @Test
  void testFindByEmail_InvalidEmail() {
    // Arrange
    String email = "nonexistent@example.com";

    when(repository.findByEmailIgnoreCase(email)).thenReturn(Optional.empty());

    // Act
    Optional<CustomerEntity> result = customerService.findByEmail(email);

    // Assert
    assertFalse(result.isPresent());
    verify(repository, times(1)).findByEmailIgnoreCase(email);
  }


}