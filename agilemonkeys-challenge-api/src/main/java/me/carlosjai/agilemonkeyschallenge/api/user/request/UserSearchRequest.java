package me.carlosjai.agilemonkeyschallenge.api.user.request;

import java.io.Serial;
import lombok.Data;
import me.carlosjai.agilemonkeyschallenge.api.base.request.BasePageRequest;

@Data
public class UserSearchRequest extends BasePageRequest {

  @Serial
  private static final long serialVersionUID = -4827105898939343758L;
  private String username;
  private String fullName;
  private Boolean isAdmin;
}
