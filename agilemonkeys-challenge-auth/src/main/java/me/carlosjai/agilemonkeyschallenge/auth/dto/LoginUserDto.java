package me.carlosjai.agilemonkeyschallenge.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginUserDto {

  private final String username;
  private final String password;
}
