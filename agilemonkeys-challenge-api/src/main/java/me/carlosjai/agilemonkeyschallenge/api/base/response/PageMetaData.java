package me.carlosjai.agilemonkeyschallenge.api.base.response;

import java.io.Serial;
import java.io.Serializable;
import lombok.Builder;
import lombok.Getter;

/**
 * Represents the metadata for a paginated response, including details about the current page, page
 * size, total number of pages, and total number of elements.
 */
@Getter
@Builder
public class PageMetaData implements Serializable {

  @Serial
  private static final long serialVersionUID = -4831590841220349667L;
  private final int currentPage;
  private final int pageSize;
  private final int totalPages;
  private final long totalElements;
}
