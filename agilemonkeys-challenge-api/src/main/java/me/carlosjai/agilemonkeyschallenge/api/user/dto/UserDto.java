package me.carlosjai.agilemonkeyschallenge.api.user.dto;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * UserDto represents a Data Transfer Object (DTO) for user-related information. It is used to
 * transfer user data between different layers of the application, such as from the service layer to
 * the presentation layer.
 *
 * <p>This class includes essential information about a user, including their
 * unique identifier, username, full name, roles, and audit information such as who created the user
 * and when it was last modified.
 *
 * <p>This class is built using the Builder design pattern, allowing for
 * flexible construction of UserDto instances.
 */
@Data
@Builder
@AllArgsConstructor
public class UserDto implements Serializable {

  @Serial
  private static final long serialVersionUID = 3049073897507586508L;
  private Long id;
  private String username;
  private String fullName;
  private final Set<String> roles = new HashSet<>();
  private String createdBy;
  private LocalDate creationDate;
  private String lastModifiedBy;
  private LocalDate lastModifiedDate;
}
