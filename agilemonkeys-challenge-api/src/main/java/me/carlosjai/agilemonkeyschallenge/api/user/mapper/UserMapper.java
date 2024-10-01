package me.carlosjai.agilemonkeyschallenge.api.user.mapper;

import me.carlosjai.agilemonkeyschallenge.api.user.dto.UserDto;
import me.carlosjai.agilemonkeyschallenge.domain.user.model.entity.RoleEntity;
import me.carlosjai.agilemonkeyschallenge.domain.user.model.entity.UserEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * UserMapper is an interface for mapping between UserEntity and UserDto objects. This interface is
 * annotated with {@link Mapper} to indicate that it is a MapStruct mapper, which provides automatic
 * implementation at compile-time for mapping operations.
 *
 * <p>The mapping is configured to use Spring as the component model, allowing
 * for dependency injection of this mapper in Spring-managed components.
 *
 * <p>The following mapping operations are defined:
 * <ul>
 *   <li>{@link #mapToUserDto(UserEntity)}: Maps a UserEntity object to a
 *   UserDto object.</li>
 *   <li>{@link #setRoles(UserDto, UserEntity)}: After mapping, this method
 *   populates the roles in the UserDto based on the associated RoleEntity
 *   objects.</li>
 * </ul>
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

  /**
   * Maps a UserEntity object to a UserDto object.
   *
   * @param entity the UserEntity object to be mapped
   * @return a UserDto object populated with data from the provided UserEntity
   */
  @Mapping(target = "id", source = "sk")
  UserDto mapToUserDto(UserEntity entity);

  /**
   * After the mapping from UserEntity to UserDto, this method is called to set the roles for the
   * UserDto. It checks if the RoleEntity list in the UserEntity is not empty and adds the role
   * names to the UserDto's roles set.
   *
   * @param dto    the UserDto object to which roles will be added
   * @param entity the UserEntity object from which roles are retrieved
   */
  @AfterMapping
  default void setRoles(@MappingTarget UserDto dto, UserEntity entity) {
    if (CollectionUtils.isNotEmpty(entity.getRoleEntities())) {
      dto.getRoles().addAll(entity.getRoleEntities().stream().map(RoleEntity::getName).toList());
    }
  }
}
