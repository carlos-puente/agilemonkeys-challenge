package me.carlosjai.agilemonkeyschallenge.domain.exception;

import java.io.Serial;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

@Getter
@Setter
@ToString
public class CustomResponseStatusException extends ResponseStatusException {

  @Serial
  private static final long serialVersionUID = -5698189251607913844L;
  private final String errorCode;

  public CustomResponseStatusException(HttpStatusCode status,
      String reason, String errorCode) {
    super(status, reason);
    this.errorCode = errorCode;
  }

  public CustomResponseStatusException(HttpStatusCode status,
      ErrorCodeEnum codeEnum) {
    super(status, codeEnum.getErrorMessage());
    errorCode = codeEnum.name();
  }
}
