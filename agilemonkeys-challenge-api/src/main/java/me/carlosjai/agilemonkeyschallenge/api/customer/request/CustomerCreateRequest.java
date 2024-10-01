package me.carlosjai.agilemonkeyschallenge.api.customer.request;

import jakarta.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;
import lombok.Data;

/**
 * CustomerCreateRequest is a Data Transfer Object (DTO) used for creating new customer records in
 * the system. It encapsulates the necessary details required for creating a customer, such as their
 * name, email, and phone number.
 *
 * <p>This class is annotated with {@link Data} to automatically generate
 * getters, setters, and utility methods like equals, hashCode, and toString.
 *
 * <p>This class implements {@link Serializable} to support object serialization.
 *
 * <p>Validation annotations are used to enforce non-null and non-blank constraints
 * on fields to ensure that no empty values are accepted when creating a customer.
 *
 * <p>Fields:
 * <ul>
 *   <li>{@code firstName}: The first name of the customer, cannot be blank.</li>
 *   <li>{@code lastName}: The last name of the customer, cannot be blank.</li>
 *   <li>{@code email}: The email address of the customer, cannot be blank.</li>
 *   <li>{@code phoneNumber}: The phone number of the customer, cannot be blank.</li>
 * </ul>
 */
@Data
public class CustomerCreateRequest implements Serializable {

  @Serial
  private static final long serialVersionUID = -3931609887685435158L;
  @NotBlank
  private String firstName;
  @NotBlank
  private String lastName;
  @NotBlank
  private String email;
  @NotBlank
  private String phoneNumber;
}
