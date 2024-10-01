package me.carlosjai.agilemonkeyschallenge.api.customer.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import me.carlosjai.agilemonkeyschallenge.api.base.response.BasePageResponse;
import me.carlosjai.agilemonkeyschallenge.api.customer.dto.CustomerDto;
import me.carlosjai.agilemonkeyschallenge.api.customer.request.CustomerCreateRequest;
import me.carlosjai.agilemonkeyschallenge.api.customer.request.CustomerSearchRequest;
import me.carlosjai.agilemonkeyschallenge.api.customer.request.CustomerUpdateRequest;
import me.carlosjai.agilemonkeyschallenge.api.customer.service.CustomerServiceAdapter;
import me.carlosjai.agilemonkeyschallenge.api.customer.validation.ContentTypeValidator;
import me.carlosjai.agilemonkeyschallenge.domain.constants.Constants;
import me.carlosjai.agilemonkeyschallenge.domain.exception.CustomResponseStatusException;
import me.carlosjai.agilemonkeyschallenge.domain.exception.ErrorCodeEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * CustomerController handles the management of customer-related operations in the application. It
 * exposes endpoints for creating, retrieving, updating, deleting, and managing customer data and
 * customer-related resources such as pictures.
 *
 * <p>This controller is mapped to the base URL {@code /api/v1/customer} and includes
 * various endpoints to perform CRUD operations. It is secured with role-based authorization,
 * allowing access to users with 'ADMIN' or 'USER' roles.
 *
 * <p>The controller uses a {@link CustomerServiceAdapter} to interact with the
 * underlying service layer and a {@link ContentTypeValidator} to validate file types during picture
 * upload.
 *
 * <p>Endpoints in this controller:
 * <ul>
 *   <li>{@link #getCustomerDetail(String)}: Retrieves a customer by ID.</li>
 *   <li>{@link #getAllCustomers(CustomerSearchRequest)}: Retrieves a paginated list of customers based on search criteria.</li>
 *   <li>{@link #createCustomer(CustomerCreateRequest)}: Creates a new customer.</li>
 *   <li>{@link #updateCustomer(CustomerUpdateRequest)}: Updates an existing customer.</li>
 *   <li>{@link #deleteCustomer(String)}: Deletes a customer by ID.</li>
 *   <li>{@link #downloadCustomerPicture(String, HttpServletResponse)}: Downloads a customer's picture.</li>
 *   <li>{@link #uploadCustomerPicture(MultiValueMap, String)}: Uploads a picture for a customer.</li>
 * </ul>
 *
 * <p>The controller uses {@link ValidatorFactory} to validate requests, ensuring
 * that the input data adheres to the required constraints.
 *
 * <p>All operations return a {@link ResponseEntity} containing the appropriate
 * response body and HTTP status code.
 */
@RequestMapping(Constants.API + Constants.V1 + "/customer")
@Controller
@AllArgsConstructor
public class CustomerController {

  private final CustomerServiceAdapter customerServiceAdapter;
  private final ContentTypeValidator contentTypeValidator;
  private static final Logger log = LoggerFactory.getLogger(CustomerController.class);

  private Validator getValidator() {
    try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
      return factory.getValidator();
    }
  }

  /**
   * Retrieves the details of a customer by their ID.
   *
   * @param customerId the ID of the customer to retrieve
   * @return a {@link ResponseEntity} containing the {@link CustomerDto} and an HTTP 200 status
   */
  @PreAuthorize("hasAnyRole('ADMIN','USER')")
  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CustomerDto> getCustomerDetail(
      @PathVariable(Constants.ID_PARAM) final String customerId) {
    if (log.isDebugEnabled()) {
      log.debug("getCustomerDetail: {}", customerId);
    }
    return ResponseEntity.ok(customerServiceAdapter.getCustomerDetail(customerId));
  }

  /**
   * Retrieves a paginated list of customers based on search criteria.
   *
   * @param customerSearchRequest the search criteria
   * @return a {@link ResponseEntity} containing a page of {@link CustomerDto} and an HTTP 200
   * status
   */
  @PreAuthorize("hasAnyRole('ADMIN','USER')")
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BasePageResponse<CustomerDto>> getAllCustomers(
      @RequestBody final CustomerSearchRequest customerSearchRequest) {
    if (log.isDebugEnabled()) {
      log.debug("getAllCustomers: {}", customerSearchRequest);
    }
    return ResponseEntity.status(HttpStatus.OK)
        .body(customerServiceAdapter.getAllCustomers(customerSearchRequest));
  }

  /**
   * Creates a new customer.
   *
   * @param createRequest the request containing customer creation data
   * @return a {@link ResponseEntity} containing the created {@link CustomerDto} and an HTTP 201
   * status
   */
  @PreAuthorize("hasAnyRole('ADMIN','USER')")
  @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CustomerDto> createCustomer(
      @Valid @RequestBody final CustomerCreateRequest createRequest) {
    if (log.isDebugEnabled()) {
      log.debug("createCustomer: {}", createRequest);
    }
    var createdCustomer = customerServiceAdapter.createCustomer(createRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
  }

  /**
   * Updates an existing customer.
   *
   * @param updateRequest the request containing customer update data
   * @return a {@link ResponseEntity} containing the updated {@link CustomerDto} and an HTTP 200
   * status
   */
  @PreAuthorize("hasAnyRole('ADMIN','USER')")
  @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CustomerDto> updateCustomer(
      @Valid @RequestBody final CustomerUpdateRequest updateRequest) {
    if (log.isDebugEnabled()) {
      log.debug("updateCustomer: {}", updateRequest);
    }
    var updatedCustomer = customerServiceAdapter.updateCustomer(updateRequest);
    return ResponseEntity.status(HttpStatus.OK).body(updatedCustomer);
  }

  /**
   * Deletes a customer by their ID.
   *
   * @param customerId the ID of the customer to delete
   * @return a {@link ResponseEntity} with an HTTP 204 No Content status
   */
  @PreAuthorize("hasAnyRole('ADMIN','USER')")
  @DeleteMapping(value = "/{id}/delete", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> deleteCustomer(
      @PathVariable(Constants.ID_PARAM) String customerId) {
    if (log.isDebugEnabled()) {
      log.debug("deleteCustomer: {}", customerId);
    }
    customerServiceAdapter.deleteCustomer(customerId);
    return ResponseEntity.noContent().build();
  }

  /**
   * Downloads the picture associated with a customer by their ID.
   *
   * @param customerId the ID of the customer whose picture is to be downloaded
   * @param response   the {@link HttpServletResponse} for writing the picture data
   */
  @PreAuthorize("hasAnyRole('ADMIN','USER')")
  @GetMapping(value = "/{id}/picture/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
  public void downloadCustomerPicture(@PathVariable(Constants.ID_PARAM) String customerId,
      final HttpServletResponse response) {
    if (log.isDebugEnabled()) {
      log.debug("downloadCustomerPicture: {}", customerId);
    }
    customerServiceAdapter.downloadCustomerPicture(customerId, response);

  }

  /**
   * Uploads a picture for a customer by their ID. Validates the file types before uploading.
   *
   * @param allFileParams the map of file data to be uploaded
   * @param customerId    the ID of the customer
   * @return a {@link ResponseEntity} indicating success or failure of the upload
   */
  @PreAuthorize("hasAnyRole('ADMIN','USER')")
  @PostMapping(value = "/{id}/picture/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> uploadCustomerPicture(
      @RequestParam MultiValueMap<String, MultipartFile> allFileParams,
      @PathVariable(Constants.ID_PARAM) String customerId) {
    if (log.isDebugEnabled()) {
      log.debug("uploadCustomerPicture: customerId {}, allFileParams {}", customerId,
          allFileParams);
    }
    List<String> nonValidFiles = contentTypeValidator.validate(allFileParams);
    if (CollectionUtils.isEmpty(nonValidFiles)) {
      customerServiceAdapter.uploadCustomerPicture(allFileParams, customerId);
    } else {
      throw new CustomResponseStatusException(HttpStatus.BAD_REQUEST,
          ErrorCodeEnum.CUSTOMER_PICTURE_NOT_VALID.getErrorMessage() + nonValidFiles.stream()
              .map(String::valueOf)
              .collect(
                  Collectors.joining()), ErrorCodeEnum.CUSTOMER_PICTURE_NOT_VALID.name());
    }
    return ResponseEntity.ok().body("Image uploaded successfully.");
  }
}
