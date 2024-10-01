package me.carlosjai.agilemonkeyschallenge.api.customer.service;

import jakarta.servlet.http.HttpServletResponse;
import me.carlosjai.agilemonkeyschallenge.api.base.response.BasePageResponse;
import me.carlosjai.agilemonkeyschallenge.api.customer.dto.CustomerDto;
import me.carlosjai.agilemonkeyschallenge.api.customer.request.CustomerCreateRequest;
import me.carlosjai.agilemonkeyschallenge.api.customer.request.CustomerSearchRequest;
import me.carlosjai.agilemonkeyschallenge.api.customer.request.CustomerUpdateRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

/**
 * CustomerServiceAdapter is an interface that defines the operations for managing customer-related
 * data in the system. It serves as an abstraction layer between the application and the underlying
 * data access layer, allowing for the manipulation and retrieval of customer records.
 *
 * <p>This interface includes methods for creating, updating, deleting,
 * and retrieving customer information, as well as handling customer pictures.
 */
public interface CustomerServiceAdapter {

  /**
   * Deletes a customer record from the system based on the provided customer ID.
   *
   * @param customerId the unique identifier of the customer to be deleted
   */
  void deleteCustomer(String customerId);

  /**
   * Retrieves the details of a specific customer identified by the provided customer ID.
   *
   * @param customerId the unique identifier of the customer whose details are to be retrieved
   * @return a {@link CustomerDto} containing the details of the customer
   */
  CustomerDto getCustomerDetail(String customerId);

  /**
   * Retrieves a paginated list of all customers that match the criteria specified in the
   * {@link CustomerSearchRequest}.
   *
   * @param customerSearchRequest the search criteria for filtering customers
   * @return a {@link BasePageResponse<CustomerDto>} containing a list of customers and pagination
   * information
   */
  BasePageResponse<CustomerDto> getAllCustomers(CustomerSearchRequest customerSearchRequest);

  /**
   * Creates a new customer record in the system based on the provided details.
   *
   * @param createRequest the details of the customer to be created
   * @return a {@link CustomerDto} containing the details of the newly created customer
   */
  CustomerDto createCustomer(CustomerCreateRequest createRequest);

  /**
   * Updates an existing customer record in the system based on the provided details.
   *
   * @param updateRequest the details of the customer to be updated
   * @return a {@link CustomerDto} containing the updated details of the customer
   */
  CustomerDto updateCustomer(CustomerUpdateRequest updateRequest);

  /**
   * Uploads a customer picture for the specified customer identified by the customer ID.
   *
   * @param allFileParams the files to be uploaded as customer pictures
   * @param customerId    the unique identifier of the customer for whom the picture is to be
   *                      uploaded
   */
  void uploadCustomerPicture(MultiValueMap<String, MultipartFile> allFileParams, String customerId);

  /**
   * Downloads the customer picture associated with the specified customer ID.
   *
   * @param customerId the unique identifier of the customer whose picture is to be downloaded
   * @param response   the {@link HttpServletResponse} object to write the picture data to
   */
  void downloadCustomerPicture(String customerId, HttpServletResponse response);
}
