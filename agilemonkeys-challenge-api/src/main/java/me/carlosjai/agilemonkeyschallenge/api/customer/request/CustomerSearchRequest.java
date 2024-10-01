package me.carlosjai.agilemonkeyschallenge.api.customer.request;

import java.io.Serial;
import lombok.Data;
import me.carlosjai.agilemonkeyschallenge.api.base.request.BasePageRequest;

/**
 * CustomerSearchRequest is a Data Transfer Object (DTO) used for searching customer records in the
 * system. It allows filtering based on various customer attributes such as their ID, name, email,
 * and phone number.
 *
 * <p>This class extends {@link BasePageRequest}, which likely provides
 * pagination and sorting capabilities to handle large data sets.
 *
 * <p>This class is annotated with {@link Data} to automatically generate
 * getters, setters, and utility methods like equals, hashCode, and toString.
 *
 * <p>Fields:
 * <ul>
 *   <li>{@code id}: The unique identifier of the customer to search for.</li>
 *   <li>{@code firstName}: The first name of the customer to search for.</li>
 *   <li>{@code lastName}: The last name of the customer to search for.</li>
 *   <li>{@code email}: The email address of the customer to search for.</li>
 *   <li>{@code phoneNumber}: The phone number of the customer to search for.</li>
 * </ul>
 */
@Data
public class CustomerSearchRequest extends BasePageRequest {

  @Serial
  private static final long serialVersionUID = 979086921163607242L;
  private Long id;
  private String firstName;
  private String lastName;
  private String email;
  private String phoneNumber;
}
