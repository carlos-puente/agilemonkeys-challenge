package me.carlosjai.agilemonkeyschallenge.api.base.response;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A generic class that represents a paginated response. This class is used to return a list of
 * content along with pagination metadata in API responses.
 *
 * <p>It is designed to work with any type of content, as specified by the generic type parameter
 * {@code <A>}.</p>
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class BasePageResponse<A> implements Serializable {

  @Serial
  private static final long serialVersionUID = -3314791722034652424L;
  private List<A> content;
  private PageMetaData metaData;
}
