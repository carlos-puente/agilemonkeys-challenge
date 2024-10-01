package me.carlosjai.agilemonkeyschallenge.domain.auditable;

import java.util.Objects;
import java.util.Optional;
import me.carlosjai.agilemonkeyschallenge.domain.user.model.entity.UserEntity;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuditorAwareImpl implements AuditorAware<String> {

  @Override
  public Optional<String> getCurrentAuditor() {
    var principal = SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();
    if (principal instanceof String && "anonymousUser".equalsIgnoreCase(principal.toString())) {
      return Optional.of("SIGNUP-PROCESS");
    } else {
      UserEntity currentUser = (UserEntity) principal;
      return Optional.ofNullable(Objects.nonNull(currentUser) ? currentUser.getUsername() : null);
    }
  }
}
