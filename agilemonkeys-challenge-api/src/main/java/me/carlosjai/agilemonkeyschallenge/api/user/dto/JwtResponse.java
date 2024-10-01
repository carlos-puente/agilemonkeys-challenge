package me.carlosjai.agilemonkeyschallenge.api.user.dto;

import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtResponse implements Serializable {

  @Serial
  private static final long serialVersionUID = 2302130474697363190L;
  private String accessToken;
  private long expiresIn;
}
