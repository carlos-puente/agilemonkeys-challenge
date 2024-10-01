package me.carlosjai.agilemonkeyschallenge.api.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import me.carlosjai.agilemonkeyschallenge.domain.exception.CustomResponseStatusException;
import me.carlosjai.agilemonkeyschallenge.domain.exception.ErrorCodeEnum;
import me.carlosjai.agilemonkeyschallenge.domain.user.definition.RoleEnum;
import me.carlosjai.agilemonkeyschallenge.domain.user.model.entity.RoleEntity;
import me.carlosjai.agilemonkeyschallenge.domain.user.model.entity.UserEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

public final class RoleUtil {

  public static void validateRoles(Set<String> roles) {
    Set<String> validRoles = Arrays.stream(RoleEnum.values())
        .map(Enum::name)
        .collect(Collectors.toSet());

    Set<String> invalidRoles = roles.stream()
        .filter(role -> !validRoles.contains(role))
        .collect(Collectors.toSet());

    if (!invalidRoles.isEmpty()) {
      throw new CustomResponseStatusException(HttpStatus.BAD_REQUEST,
          ErrorCodeEnum.INVALID_ROLES.getErrorMessage() + invalidRoles,
          ErrorCodeEnum.INVALID_ROLES.name());
    }
  }

  public static boolean hasAdminRole(UserEntity entity) {
    return entity.getRoleEntities().stream()
        .anyMatch(role -> RoleEnum.ROLE_ADMIN.name().equals(role.getName()));
  }

  public static Set<RoleEntity> getAndFilterRolesExcluding(Set<RoleEntity> roleEntities,
      RoleEnum excludedRole) {
    return roleEntities.stream().filter(
        roleEntity -> !StringUtils.equalsIgnoreCase(roleEntity.getName(),
            excludedRole.name())).collect(
        Collectors.toSet());
  }


}
