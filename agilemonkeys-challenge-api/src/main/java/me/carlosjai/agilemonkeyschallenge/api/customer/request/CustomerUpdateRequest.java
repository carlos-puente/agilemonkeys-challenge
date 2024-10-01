package me.carlosjai.agilemonkeyschallenge.api.customer.request;

import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import lombok.Data;

/**
 * CustomerUpdateRequest is a Data Transfer Object (DTO) used for updating existing customer records
 * in the system. It encapsulates the necessary fields required to identify and modify a customer's
 * details.
 *
 * <p>This class is annotated with {@link Data} to automatically generate
 * getters, setters, and utility methods such as equals, hashCode, and toString.
 *
 * <p>Validation annotations are used to enforce constraints on the fields,
 * ensuring that valid data is provided for updates.
 *
 * <p>Fields:
 * <ul>
 *   <li>{@code id}: The unique identifier of the customer to be updated.
 *   This field cannot be null.</li>
 *   <li>{@code firstName}: The first name of the customer to be updated.
 *   This field can be updated, but may be null.</li>
 *   <li>{@code lastName}: The last name of the customer to be updated.
 *   This field can be updated, but may be null.</li>
 *   <li>{@code email}: The email address of the customer to be updated.
 *   This field can be updated, but may be null.</li>
 *   <li>{@code phoneNumber}: The phone number of the customer to be updated.
 *   This field can be updated, but may be null.</li>
 * </ul>
 */
@Data
public class CustomerUpdateRequest {

  @Serial
  private static final long serialVersionUID = -1710233029152799631L;
  @NotNull
  private Long id;
  private String firstName;
  private String lastName;
  private String email;
  private String phoneNumber;
}
