package me.carlosjai.agilemonkeyschallenge.api.customer.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.List;
import me.carlosjai.agilemonkeyschallenge.api.base.response.BasePageResponse;
import me.carlosjai.agilemonkeyschallenge.api.customer.dto.CustomerDto;
import me.carlosjai.agilemonkeyschallenge.api.customer.request.CustomerCreateRequest;
import me.carlosjai.agilemonkeyschallenge.api.customer.request.CustomerSearchRequest;
import me.carlosjai.agilemonkeyschallenge.api.customer.request.CustomerUpdateRequest;
import me.carlosjai.agilemonkeyschallenge.api.customer.service.CustomerServiceAdapter;
import me.carlosjai.agilemonkeyschallenge.api.customer.validation.ContentTypeValidator;
import me.carlosjai.agilemonkeyschallenge.api.data.DataGenerator;
import me.carlosjai.agilemonkeyschallenge.auth.jwt.service.JwtService;
import me.carlosjai.agilemonkeyschallenge.auth.userdetail.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private CustomerServiceAdapter customerServiceAdapter;
  @MockBean
  private ContentTypeValidator contentTypeValidator;
  @MockBean
  private JwtService service;
  @MockBean
  private UserDetailsServiceImpl userDetailsService;
  @Autowired
  private ObjectMapper objectMapper;


  @Test
  void testGetCustomerDetail_Success() throws Exception {
    String customerId = "1";
    CustomerDto customerDto = DataGenerator.generateCustomerDto(1L);

    when(customerServiceAdapter.getCustomerDetail(customerId)).thenReturn(customerDto);
    mockMvc.perform(get("/api/v1/customer/{id}", customerId)
            .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.firstName").value("firstNameTest1"))
        .andExpect(jsonPath("$.lastName").value("lastNameTest1"))
        .andExpect(jsonPath("$.email").value("email1@test.com"));
  }

  @Test
  void testCreateCustomer_Success() throws Exception {
    var createRequest = DataGenerator.generateCustomerCreateRequest(1L);
    var customerDto = DataGenerator.generateCustomerDto(1L);

    when(customerServiceAdapter.createCustomer(any(CustomerCreateRequest.class))).thenReturn(
        customerDto);

    mockMvc.perform(post("/api/v1/customer/create")
            .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createRequest)))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.firstName").value("firstNameTest1"))
        .andExpect(jsonPath("$.lastName").value("lastNameTest1"))
        .andExpect(jsonPath("$.email").value("email1@test.com"));
  }

  @Test
  void testCreateCustomer_FailValidation_throwsBadRequest() throws Exception {
    var createRequest = DataGenerator.generateCustomerCreateRequest(1L);
    createRequest.setEmail(null);

    mockMvc.perform(post("/api/v1/customer/create")
            .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createRequest)))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message[0]").value("email: must not be blank"))
        .andExpect(jsonPath("$.status").value("Bad Request"))
        .andExpect(jsonPath("$.errorCode").value("VALIDATION"));

    createRequest.setEmail("test@email.com");
    createRequest.setFirstName(null);

    mockMvc.perform(post("/api/v1/customer/create")
            .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createRequest)))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message[0]").value("firstName: must not be blank"))
        .andExpect(jsonPath("$.status").value("Bad Request"))
        .andExpect(jsonPath("$.errorCode").value("VALIDATION"));

    createRequest.setFirstName("testFirstName");
    createRequest.setLastName(null);

    mockMvc.perform(post("/api/v1/customer/create")
            .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createRequest)))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message[0]").value("lastName: must not be blank"))
        .andExpect(jsonPath("$.status").value("Bad Request"))
        .andExpect(jsonPath("$.errorCode").value("VALIDATION"));

    createRequest.setLastName("testLastName");
    createRequest.setPhoneNumber(null);

    mockMvc.perform(post("/api/v1/customer/create")
            .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createRequest)))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message[0]").value("phoneNumber: must not be blank"))
        .andExpect(jsonPath("$.status").value("Bad Request"))
        .andExpect(jsonPath("$.errorCode").value("VALIDATION"));

    var emptyRequest = new CustomerCreateRequest();

    mockMvc.perform(post("/api/v1/customer/create")
            .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(emptyRequest)))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message.size()").value(4))
        .andExpect(jsonPath("$.status").value("Bad Request"))
        .andExpect(jsonPath("$.errorCode").value("VALIDATION"));

  }

  @Test
  void testGetAllCustomers_Success() throws Exception {
    CustomerSearchRequest searchRequest = new CustomerSearchRequest();
    BasePageResponse<CustomerDto> response = new BasePageResponse<>();

    when(customerServiceAdapter.getAllCustomers(any(CustomerSearchRequest.class)))
        .thenReturn(response);
    mockMvc.perform(post("/api/v1/customer")
            .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))

            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(searchRequest)))
        .andDo(print())
        .andExpect(status().isOk());
  }


  @Test
  void testUpdateCustomer_Success() throws Exception {
    var updateRequest = DataGenerator.generateCustomerUpdateRequest(1L);
    var updatedCustomer = DataGenerator.generateCustomerDto(1L);

    when(customerServiceAdapter.updateCustomer(any(CustomerUpdateRequest.class)))
        .thenReturn(updatedCustomer);

    mockMvc.perform(put("/api/v1/customer/update")
            .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateRequest)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.firstName").value("Jane"))
        .andExpect(jsonPath("$.lastName").value("Doe"))
        .andExpect(jsonPath("$.email").value("jane.doe@example.com"));
  }

  @Test
  void testUpdateCustomer_ValidationFail() throws Exception {
    var updateRequest = new CustomerUpdateRequest();

    mockMvc.perform(put("/api/v1/customer/update")
            .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateRequest)))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message[0]").value("id: must not be null"))
        .andExpect(jsonPath("$.status").value("Bad Request"))
        .andExpect(jsonPath("$.errorCode").value("VALIDATION"));
  }


  @Test
  void testDeleteCustomer_Success() throws Exception {
    String customerId = "1";

    doNothing().when(customerServiceAdapter).deleteCustomer(customerId);

    mockMvc.perform(delete("/api/v1/customer/{id}/delete", customerId)
            .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isNoContent());
  }

  @Test
  void testUploadCustomerPicture_Success() throws Exception {
    String customerId = "1";
    MultiValueMap<String, MultipartFile> files = new LinkedMultiValueMap<>();

    when(contentTypeValidator.validate(files)).thenReturn(Collections.emptyList());
    doNothing().when(customerServiceAdapter).uploadCustomerPicture(files, customerId);

    mockMvc.perform(post("/api/v1/customer/{id}/picture/upload", customerId)
            .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .content(objectMapper.writeValueAsString(files)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string("Image uploaded successfully."));
  }

  @Test
  void testUploadCustomerPicture_InvalidFileType() throws Exception {
    String customerId = "1";
    MultiValueMap<String, MultipartFile> files = new LinkedMultiValueMap<>();
    List<String> nonValidFiles = List.of("invalidFile.txt");

    when(contentTypeValidator.validate(files)).thenReturn(nonValidFiles);

    mockMvc.perform(post("/api/v1/customer/{id}/picture/upload", customerId)
            .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .content(objectMapper.writeValueAsString(files)))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("There are not supported files: invalidFile.txt"));
  }
}