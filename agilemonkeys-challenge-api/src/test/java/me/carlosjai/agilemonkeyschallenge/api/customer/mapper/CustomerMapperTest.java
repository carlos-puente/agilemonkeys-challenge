package me.carlosjai.agilemonkeyschallenge.api.customer.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import me.carlosjai.agilemonkeyschallenge.api.customer.dto.CustomerDto;
import me.carlosjai.agilemonkeyschallenge.domain.customer.model.entity.CustomerEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

public class CustomerMapperTest {

  private CustomerMapper customerMapper;

  @BeforeEach
  void setUp() {
    customerMapper = Mappers.getMapper(CustomerMapper.class);
  }

  @Test
  void testMapToCustomerDto() {
    CustomerEntity entity = me.carlosjai.agilemonkeyschallenge.api.data.DataGenerator.generateCustomerEntity(
        1L);
    CustomerDto dto = customerMapper.mapToCustomerDto(entity);
    assertEquals(entity.getSk(), dto.getId());
    assertEquals(entity.getFirstName(), dto.getFirstName());
    assertEquals(entity.getLastName(), dto.getLastName());
    assertEquals(entity.getEmail(), dto.getEmail());
    assertEquals(entity.getPhoneNumber(), dto.getPhoneNumber());
    assertEquals(entity.getPicture(), dto.getPicture());
  }

  @Test
  void testMapToCustomerDto_withNullFields() {
    CustomerEntity entity = me.carlosjai.agilemonkeyschallenge.api.data.DataGenerator.generateCustomerEntity(
        1L);
    CustomerDto dto = customerMapper.mapToCustomerDto(entity);
    assertEquals(entity.getSk(), dto.getId());
    assertEquals(entity.getFirstName(), dto.getFirstName());
    assertEquals(entity.getLastName(), dto.getLastName());
    assertEquals(entity.getEmail(), dto.getEmail());
    assertEquals(entity.getPhoneNumber(), dto.getPhoneNumber());
    assertEquals(entity.getPicture(), dto.getPicture());
  }

  @Test
  void testMapToCustomerDto_NullEntity() {
    CustomerDto dto = customerMapper.mapToCustomerDto(null);
    assertNull(dto);
  }
}