package me.carlosjai.agilemonkeyschallenge.api.base.response;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 * ErrorResponse is a model class used to represent the structure of an error response that is
 * returned by the API when an exception occurs. This class provides details about the error,
 * including a list of messages, a timestamp, the HTTP status, and an optional error code.
 *
 * <p>This class is serializable and is typically used in the context of
 * global exception handling to provide consistent error messages to clients.
 *
 * <p>The class is annotated with {@link Data} and {@link Builder},
 * allowing for convenient generation of getters, setters, equals/hashCode, and the builder
 * pattern.
 *
 * <p>Fields:
 * <ul>
 *   <li>{@code message}: A list of error messages providing details about the error(s).</li>
 *   <li>{@code timestamp}: The time when the error occurred.</li>
 *   <li>{@code status}: The HTTP status associated with the error.</li>
 *   <li>{@code errorCode}: An optional field to include application-specific error codes.</li>
 * </ul>
 */
@Data
@Builder
public class ErrorResponse implements Serializable {

  @Serial
  private static final long serialVersionUID = -1998453030357247787L;
  private List<String> message = new ArrayList<>();
  private LocalDateTime timestamp;
  private String status;
  private String errorCode;
}
