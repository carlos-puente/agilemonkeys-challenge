package me.carlosjai.agilemonkeyschallenge.api.user.request;

import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;

@Data
public class UserUpdateRequest implements Serializable {

  @Serial
  private static final long serialVersionUID = -4444950759305208923L;
  @NotNull
  private Long id;
  private String password;
  private String fullname;
  private Set<String> roles = new HashSet<>();
}
