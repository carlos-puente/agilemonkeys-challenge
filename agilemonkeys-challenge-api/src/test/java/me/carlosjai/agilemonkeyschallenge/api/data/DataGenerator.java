package me.carlosjai.agilemonkeyschallenge.api.data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Set;
import me.carlosjai.agilemonkeyschallenge.api.customer.dto.CustomerDto;
import me.carlosjai.agilemonkeyschallenge.api.customer.request.CustomerCreateRequest;
import me.carlosjai.agilemonkeyschallenge.api.customer.request.CustomerUpdateRequest;
import me.carlosjai.agilemonkeyschallenge.domain.customer.model.entity.CustomerEntity;
import me.carlosjai.agilemonkeyschallenge.domain.user.model.entity.RoleEntity;
import me.carlosjai.agilemonkeyschallenge.domain.user.model.entity.UserEntity;

public final class DataGenerator {

  public static CustomerDto generateCustomerDto(Long id) {
    return CustomerDto.builder()
        .id(id)
        .firstName("firstNameTest" + id)
        .lastName("lastNameTest" + id)
        .email("email" + id + "@test.com")
        .phoneNumber("625535" + id)
        .createdBy("TEST")
        .creationDate(LocalDate.now())
        .lastModifiedBy("TEST")
        .lastModifiedDate(LocalDate.now())
        .build();
  }

  public static CustomerCreateRequest generateCustomerCreateRequest(Long id) {
    var createRequest = new CustomerCreateRequest();
    createRequest.setFirstName("firstNameTest" + id);
    createRequest.setLastName("lastNameTest" + id);
    createRequest.setEmail("email" + id + "@test.com");
    createRequest.setPhoneNumber("625535" + id);

    return createRequest;
  }

  public static CustomerUpdateRequest generateCustomerUpdateRequest(Long id) {
    var createRequest = new CustomerUpdateRequest();
    createRequest.setId(id);
    createRequest.setFirstName("firstNameTest" + id);
    createRequest.setLastName("lastNameTest" + id);
    createRequest.setEmail("email" + id + "@test.com");
    createRequest.setPhoneNumber("625535" + id);

    return createRequest;
  }

  public static UserEntity generateUserEntity(Long id) {
    var entity = new UserEntity();
    entity.setSk(1L);
    entity.setUsername("testUser");
    entity.setFullName("Test User");
    entity.setCreatedBy("admin");
    entity.setCreationDate(
        Date.from(LocalDateTime.of(2022, 1, 1, 12, 0).toInstant(ZoneOffset.UTC)));
    entity.setLastModifiedBy("admin");
    entity.setLastModifiedDate(
        Date.from(LocalDateTime.of(2023, 1, 1, 12, 0).toInstant(ZoneOffset.UTC)));
    entity.setRoleEntities(Set.of(new RoleEntity(1L, "ROLE_USER")));
    return entity;
  }

  public static CustomerEntity generateCustomerEntity(Long id) {
    var entity = new CustomerEntity();
    entity.setSk(1L);
    entity.setFirstName("John");
    entity.setLastName("Doe");
    entity.setEmail("john.doe@example.com");
    entity.setPhoneNumber("123456789");
    entity.setPicture("customer1.jpg");
    entity.setPictureBytes("customer1.jpg".getBytes());
    return entity;
  }
}
