package me.carlosjai.agilemonkeyschallenge.api.base.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.carlosjai.agilemonkeyschallenge.domain.constants.Constants;


/**
 * A base class for handling paginated requests, containing common parameters such as search term,
 * current page, page size, sort field, and sort order. This class is used to encapsulate the paging
 * and sorting details for API requests that require pagination.
 *
 * <p>It implements {@link Serializable} to allow easy transmission of instances, and uses Jackson
 * annotations for JSON property mapping.</p>
 */
@Getter
@Setter
@ToString
public class BasePageRequest implements Serializable {

  @Serial
  private static final long serialVersionUID = -3237580894252249271L;
  @JsonProperty(Constants.QUERY_PARAM)
  protected String searchTerm;
  @JsonProperty(Constants.CURRENT_PAGE_PARAM)
  protected String currentPage;
  @JsonProperty(Constants.PAGE_SIZE_PARAM)
  protected String pageSize;
  @JsonProperty(Constants.SORT_BY_PARAM)
  protected String sortBy;
  @JsonProperty(Constants.SORT_ORDER_PARAM)
  protected String sortOrder;

}
