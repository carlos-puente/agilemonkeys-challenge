package me.carlosjai.agilemonkeyschallenge.api.user.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import me.carlosjai.agilemonkeyschallenge.domain.constants.Constants;

@Data
public class UserCreateRequest implements Serializable {

  @Serial
  private static final long serialVersionUID = 456367822333215654L;
  @NotBlank
  private String username;
  @Pattern(
      regexp = Constants.REGEX_PASSWORD,
      message = Constants.ERROR_WRONG_PASSWORD
  )
  private String password;
  @NotBlank
  private String fullname;
  private Set<String> roles = new HashSet<>();

}
