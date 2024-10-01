package me.carlosjai.agilemonkeyschallenge.api.customer.service.impl;


import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import me.carlosjai.agilemonkeyschallenge.api.customer.dto.CustomerDto;
import me.carlosjai.agilemonkeyschallenge.api.customer.mapper.CustomerMapper;
import me.carlosjai.agilemonkeyschallenge.api.customer.request.CustomerCreateRequest;
import me.carlosjai.agilemonkeyschallenge.api.customer.request.CustomerUpdateRequest;
import me.carlosjai.agilemonkeyschallenge.domain.constants.Constants;
import me.carlosjai.agilemonkeyschallenge.domain.customer.model.entity.CustomerEntity;
import me.carlosjai.agilemonkeyschallenge.domain.customer.model.service.CustomerService;
import me.carlosjai.agilemonkeyschallenge.domain.exception.CustomResponseStatusException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest(classes = CustomerServiceAdapterImpl.class)
class CustomerServiceAdapterImplTest {

  @MockBean
  private CustomerService service;

  @MockBean
  private CustomerMapper mapper;

  @Mock
  private HttpServletResponse response;

  @Autowired
  private CustomerServiceAdapterImpl customerServiceAdapter;

  private CustomerEntity customerEntity;
  private CustomerDto customerDto;
  private CustomerCreateRequest customerCreateRequest;
  private CustomerUpdateRequest customerUpdateRequest;


  @BeforeEach
  void setUp() {
    customerEntity = me.carlosjai.agilemonkeyschallenge.api.data.DataGenerator.generateCustomerEntity(
        1L);

    customerDto = CustomerDto.builder()
        .id(customerEntity.getSk())
        .firstName(customerEntity.getFirstName())
        .lastName(customerEntity.getLastName())
        .email(customerEntity.getEmail())
        .phoneNumber(customerEntity.getPhoneNumber())
        .build();

    customerCreateRequest = me.carlosjai.agilemonkeyschallenge.api.data.DataGenerator.generateCustomerCreateRequest(
        1L);

    customerUpdateRequest = me.carlosjai.agilemonkeyschallenge.api.data.DataGenerator.generateCustomerUpdateRequest(
        1L);
    customerUpdateRequest.setId(1L);
    customerUpdateRequest.setFirstName("John");
    customerUpdateRequest.setLastName("Doe");
    customerUpdateRequest.setEmail("john.doe@example.com");
    customerUpdateRequest.setPhoneNumber("123456789");
  }

  @Test
  void testGetCustomerDetail_Success() {
    when(service.getById(1L)).thenReturn(Optional.of(customerEntity));
    when(mapper.mapToCustomerDto(customerEntity)).thenReturn(customerDto);

    CustomerDto result = customerServiceAdapter.getCustomerDetail("1");

    assertNotNull(result);
    assertEquals(customerDto, result);
  }

  @Test
  void testGetCustomerDetail_NotFound() {
    when(service.getById(1L)).thenReturn(Optional.empty());

    CustomResponseStatusException exception = assertThrows(CustomResponseStatusException.class,
        () ->
            customerServiceAdapter.getCustomerDetail("1"));

    assertEquals("Customer not found with the given id.", exception.getReason());
  }

  @Test
  void testCreateCustomer_Success() {
    when(service.findByEmail(customerCreateRequest.getEmail())).thenReturn(Optional.empty());
    when(service.save(any(CustomerEntity.class))).thenReturn(customerEntity);
    when(mapper.mapToCustomerDto(customerEntity)).thenReturn(customerDto);

    CustomerDto result = customerServiceAdapter.createCustomer(customerCreateRequest);

    assertNotNull(result);
    assertEquals(customerDto, result);
  }

  @Test
  void testCreateCustomer_EmailConflict() {
    when(service.findByEmail(customerCreateRequest.getEmail())).thenReturn(
        Optional.of(customerEntity));

    CustomResponseStatusException exception = assertThrows(CustomResponseStatusException.class,
        () ->
            customerServiceAdapter.createCustomer(customerCreateRequest));

    assertEquals("The specified email is already being used.", exception.getReason());
  }

  @Test
  void testUpdateCustomer_Success() {
    when(service.getById(1L)).thenReturn(Optional.of(customerEntity));
    when(service.save(any(CustomerEntity.class))).thenReturn(customerEntity);
    when(mapper.mapToCustomerDto(customerEntity)).thenReturn(customerDto);

    CustomerDto result = customerServiceAdapter.updateCustomer(customerUpdateRequest);

    assertNotNull(result);
    assertEquals(customerDto, result);
  }

  @Test
  void testUpdateCustomer_NotFound() {
    when(service.getById(1L)).thenReturn(Optional.empty());

    CustomResponseStatusException exception = assertThrows(CustomResponseStatusException.class,
        () ->
            customerServiceAdapter.updateCustomer(customerUpdateRequest));

    assertEquals("Customer not found with the given id.", exception.getReason());
  }

  @Test
  void testDeleteCustomer_Success() {
    when(service.getById(1L)).thenReturn(Optional.of(customerEntity));

    assertDoesNotThrow(() -> customerServiceAdapter.deleteCustomer("1"));

    verify(service, times(1)).delete(customerEntity);
  }

  @Test
  void testDeleteCustomer_NotFound() {
    when(service.getById(1L)).thenReturn(Optional.empty());

    assertDoesNotThrow(() -> customerServiceAdapter.deleteCustomer("1"));

    verify(service, never()).delete(any());
  }

  @Test
  void testUploadImage_Success() {
    when(service.getById(1L)).thenReturn(Optional.of(customerEntity));
    when(service.save(any(CustomerEntity.class))).thenReturn(customerEntity);
    final MultiValueMap<String, MultipartFile> allFileParams = new LinkedMultiValueMap<>();
    allFileParams.add(Constants.PICTURE,
        new MockMultipartFile("test.png", "test.png", "application/png",
            new byte[0]));
    assertDoesNotThrow(() -> customerServiceAdapter.uploadCustomerPicture(allFileParams, "1"));
  }

  @Test
  void testUploadImage_NotFound() {
    when(service.getById(1L)).thenReturn(Optional.empty());
    when(service.save(any(CustomerEntity.class))).thenReturn(customerEntity);
    final MultiValueMap<String, MultipartFile> allFileParams = new LinkedMultiValueMap<>();
    allFileParams.add(Constants.PICTURE,
        new MockMultipartFile("test.png", "test.png", "application/png",
            new byte[0]));
    CustomResponseStatusException exception = assertThrows(CustomResponseStatusException.class,
        () ->
            customerServiceAdapter.uploadCustomerPicture(allFileParams, "1"));

    assertEquals("Customer not found with the given id.", exception.getReason());

  }

  @Test
  void testDownloadCustomerPicture_Success() throws IOException {
    customerEntity.setPicture("customer1.jpg");
    customerEntity.setPictureBytes("test".getBytes());
    MockHttpServletResponse mockedResponse = new MockHttpServletResponse();

    when(service.getById(1L)).thenReturn(Optional.of(customerEntity));

    assertDoesNotThrow(() -> customerServiceAdapter.downloadCustomerPicture("1", mockedResponse));

    assertEquals("attachment; filename=\"customer1.jpg\"",
        mockedResponse.getHeader(HttpHeaders.CONTENT_DISPOSITION));
    assertEquals(MediaType.APPLICATION_OCTET_STREAM_VALUE,
        mockedResponse.getContentType());
    assertEquals(customerEntity.getPictureBytes().length,
        mockedResponse.getContentLength());

    Assertions.assertArrayEquals(customerEntity.getPictureBytes(),
        mockedResponse.getContentAsByteArray());
  }

  @Test
  void testDownloadCustomerPicture_NotFound() {
    when(service.getById(1L)).thenReturn(Optional.empty());

    CustomResponseStatusException exception = assertThrows(CustomResponseStatusException.class,
        () ->
            customerServiceAdapter.downloadCustomerPicture("1", response));

    assertEquals("Customer not found with the given id.", exception.getReason());
  }

}