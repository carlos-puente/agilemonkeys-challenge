package me.carlosjai.agilemonkeyschallenge.domain.constants;

/**
 * Class with constants
 */
public final class Constants {

  public static final String V1 = "v1";
  public static final String ID_PARAM = "id";
  public static final String QUERY_PARAM = "q";
  public static final String CURRENT_PAGE_PARAM = "_page";
  public static final String PAGE_SIZE_PARAM = "_limit";
  public static final String SORT_BY_PARAM = "_sort";
  public static final String SORT_ORDER_PARAM = "_order";
  public static final String DEFAULT_PAGE_SIZE = "10";
  public static final String DEFAULT_CURRENT_PAGE = "1";
  public static final String DEFAULT_SORT_ORDER = "asc";

  public static final String SORT_BY_LAST_NAME = "lastName";
  public static final String SORT_BY_FIRST_NAME = "firstName";
  public static final String SORT_BY_EMAIL = "email";
  public static final String PICTURE = "picture";
  public static final String API = "/api/";
  public static final String SORT_BY_USERNAME = "username";
  public static final String SORT_BY_FULLNAME = "full_name";
  public static final String REGEX_PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
  public static final String ERROR_WRONG_PASSWORD = "Password must have at least one uppercase letter, one lowercase letter, one number, one special character, and be at least 8 characters long";
  public static final String SK = "sk";
  public static final long MAX_AGE = 10L;
}
