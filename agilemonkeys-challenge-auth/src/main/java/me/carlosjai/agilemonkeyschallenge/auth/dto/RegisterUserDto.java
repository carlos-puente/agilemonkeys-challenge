package me.carlosjai.agilemonkeyschallenge.auth.dto;

import java.io.Serial;
import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterUserDto implements Serializable {

  @Serial
  private static final long serialVersionUID = 8738164385077138086L;
  private final String username;
  private final String password;
  private final String fullName;
  
}
