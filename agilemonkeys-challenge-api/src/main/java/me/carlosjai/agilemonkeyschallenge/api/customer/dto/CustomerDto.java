package me.carlosjai.agilemonkeyschallenge.api.customer.dto;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

/**
 * CustomerDto is a Data Transfer Object (DTO) that represents customer information exchanged
 * between the client and server. It encapsulates details such as the customer's personal
 * information, contact details, and metadata about creation and modification.
 *
 * <p>This class is annotated with {@link Data} and {@link Builder},
 * which automatically generate getters, setters, constructors, and utility methods such as equals,
 * hashCode, and toString, and supports the builder pattern for creating instances.
 *
 * <p>This class implements {@link Serializable} to support object serialization.
 *
 * <p>Fields:
 * <ul>
 *   <li>{@code id}: The unique identifier of the customer.</li>
 *   <li>{@code firstName}: The first name of the customer.</li>
 *   <li>{@code lastName}: The last name of the customer.</li>
 *   <li>{@code email}: The email address of the customer.</li>
 *   <li>{@code phoneNumber}: The phone number of the customer.</li>
 *   <li>{@code picture}: A reference or path to the customer's profile picture.</li>
 *   <li>{@code createdBy}: The user who created the customer record.</li>
 *   <li>{@code creationDate}: The date when the customer record was created.</li>
 *   <li>{@code lastModifiedBy}: The user who last modified the customer record.</li>
 *   <li>{@code lastModifiedDate}: The date when the customer record was last modified.</li>
 * </ul>
 */
@Data
@Builder
public class CustomerDto implements Serializable {

  @Serial
  private static final long serialVersionUID = 4605133595028886396L;
  private Long id;
  private String firstName;
  private String lastName;
  private String email;
  private String phoneNumber;
  private String picture;
  private String createdBy;
  private LocalDate creationDate;
  private String lastModifiedBy;
  private LocalDate lastModifiedDate;
}
