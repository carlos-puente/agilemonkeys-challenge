package me.carlosjai.agilemonkeyschallenge.api.user.service.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import me.carlosjai.agilemonkeyschallenge.api.base.response.BasePageResponse;
import me.carlosjai.agilemonkeyschallenge.api.base.response.PageMetaData;
import me.carlosjai.agilemonkeyschallenge.api.user.dto.UserDto;
import me.carlosjai.agilemonkeyschallenge.api.user.mapper.UserMapper;
import me.carlosjai.agilemonkeyschallenge.api.user.request.UserCreateRequest;
import me.carlosjai.agilemonkeyschallenge.api.user.request.UserSearchRequest;
import me.carlosjai.agilemonkeyschallenge.api.user.request.UserUpdateRequest;
import me.carlosjai.agilemonkeyschallenge.api.user.service.UserServiceAdapter;
import me.carlosjai.agilemonkeyschallenge.api.util.RoleUtil;
import me.carlosjai.agilemonkeyschallenge.domain.constants.Constants;
import me.carlosjai.agilemonkeyschallenge.domain.exception.CustomResponseStatusException;
import me.carlosjai.agilemonkeyschallenge.domain.exception.ErrorCodeEnum;
import me.carlosjai.agilemonkeyschallenge.domain.user.definition.RoleEnum;
import me.carlosjai.agilemonkeyschallenge.domain.user.model.entity.QUserEntity;
import me.carlosjai.agilemonkeyschallenge.domain.user.model.entity.UserEntity;
import me.carlosjai.agilemonkeyschallenge.domain.user.model.service.RoleService;
import me.carlosjai.agilemonkeyschallenge.domain.user.model.service.UserService;
import me.carlosjai.agilemonkeyschallenge.domain.util.PasswordUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceAdapterImpl implements UserServiceAdapter {

  private final UserService userService;
  private final RoleService roleService;
  private final UserMapper mapper;
  private final PasswordEncoder passwordEncoder;
  private static final Logger log = LoggerFactory.getLogger(UserServiceAdapterImpl.class);

  @Override
  public UserDto createUser(UserCreateRequest createRequest) {
    if (log.isDebugEnabled()) {
      log.debug("createUser {}", createRequest.getUsername());
    }
    final var optExistingEntity = userService.findByUsername(createRequest.getUsername());
    if (optExistingEntity.isPresent()) {
      throw new CustomResponseStatusException(HttpStatus.CONFLICT,
          ErrorCodeEnum.USER_CREATE_USERNAME_ALREADY_EXISTS);
    } else {
      var newEntity = new UserEntity();
      newEntity.setPassword(passwordEncoder.encode(createRequest.getPassword()));
      newEntity.setUsername(createRequest.getUsername());
      newEntity.setFullName(createRequest.getFullname());
      setEntityRoles(newEntity, createRequest.getRoles());

      return mapper.mapToUserDto(userService.save(newEntity));
    }
  }

  private void setEntityRoles(UserEntity entity, Set<String> roles) {
    if (log.isDebugEnabled()) {
      log.debug("setEntityRoles {} {}", entity.getSk(), roles);
    }
    if (CollectionUtils.isEmpty(roles)) {
      entity.addRole(roleService.findByName(RoleEnum.ROLE_USER.name()));
    } else {
      RoleUtil.validateRoles(roles);
      roles.forEach(role -> entity.addRole(roleService.findByName(role)));
    }
  }

  @Override
  public void deleteUser(Long userId) {
    if (log.isDebugEnabled()) {
      log.debug("deleteUser {}", userId);
    }
    var optionalUserEntity = userService.getUserById(userId);
    if (optionalUserEntity.isPresent()) {
      userService.delete(optionalUserEntity.get());
      return;
    }
    throw new CustomResponseStatusException(HttpStatus.NOT_FOUND, ErrorCodeEnum.USER_NOT_FOUND);
  }


  @Override
  public UserDto updateUser(UserUpdateRequest updateRequest) {
    if (log.isDebugEnabled()) {
      log.debug("updateUser {}", updateRequest.getId());
    }
    var optionalUserEntity = userService.getUserById(updateRequest.getId());
    if (optionalUserEntity.isPresent()) {
      var entityToUpdate = optionalUserEntity.get();
      if (StringUtils.isNotBlank(updateRequest.getPassword())) {
        PasswordUtil.validatePassword(updateRequest.getPassword());
        entityToUpdate.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
      }
      if (StringUtils.isNotBlank(updateRequest.getFullname())) {
        entityToUpdate.setFullName(updateRequest.getFullname());
      }
      if (CollectionUtils.isNotEmpty(updateRequest.getRoles())) {
        entityToUpdate.getRoleEntities().clear();
        setEntityRoles(entityToUpdate, updateRequest.getRoles());
      }
      return mapper.mapToUserDto(userService.save(entityToUpdate));
    }
    throw new CustomResponseStatusException(HttpStatus.NOT_FOUND, ErrorCodeEnum.USER_NOT_FOUND);
  }

  @Override
  public BasePageResponse<UserDto> listUsers(UserSearchRequest userSearchRequest) {
    if (log.isDebugEnabled()) {
      log.debug("listUsers {}", userSearchRequest);
    }
    final var predicate = genereratePredicate(userSearchRequest);
    final var pageable = getPageable(userSearchRequest);
    final var pageEntities = userService.findAllBy(predicate, pageable);

    final var content = pageEntities.getContent().stream().map(mapper::mapToUserDto).toList();
    final var metaData = PageMetaData.builder().currentPage(pageEntities.getNumber() + 1)
        .pageSize(pageEntities.getSize())
        .totalPages(pageEntities.getTotalPages())
        .totalElements(pageEntities.getTotalElements())
        .build();
    return BasePageResponse.<UserDto>builder().content(content).metaData(metaData).build();

  }

  private static Pageable getPageable(final UserSearchRequest userSearchRequest) {
    if (log.isDebugEnabled()) {
      log.debug("getPageable {}", userSearchRequest);
    }
    final var pageSize = Integer.valueOf(
        StringUtils.defaultIfBlank(userSearchRequest.getPageSize(),
            Constants.DEFAULT_PAGE_SIZE));
    final var currentPage = Integer.valueOf(
        StringUtils.defaultIfBlank(userSearchRequest.getCurrentPage(),
            Constants.DEFAULT_CURRENT_PAGE));
    return PageRequest.of(currentPage - 1, pageSize, createSort(userSearchRequest));
  }

  private static Sort createSort(final UserSearchRequest userSearchRequest) {
    if (log.isDebugEnabled()) {
      log.debug("createSort {}", userSearchRequest);
    }
    var sort = Sort.by(Sort.Order.asc(Constants.SORT_BY_USERNAME));
    if (StringUtils.isNotBlank(userSearchRequest.getSortBy())) {
      if (Constants.ID_PARAM.equalsIgnoreCase(userSearchRequest.getSortBy())) {
        userSearchRequest.setSortBy(Constants.SK);
      }
      sort = generateCustomSortBy(userSearchRequest.getSortBy(),
          userSearchRequest.getSortOrder());
    }
    return sort;
  }

  private static Sort generateCustomSortBy(final String sortBy, final String sortOrder) {
    if (log.isDebugEnabled()) {
      log.debug("createSort {} {}", sortBy, sortOrder);
    }
    return StringUtils.isNotBlank(sortOrder)
        && StringUtils.equalsIgnoreCase(sortOrder, Constants.DEFAULT_SORT_ORDER) ?
        Sort.by(Order.asc(sortBy)) : Sort.by(Order.desc(sortBy));

  }

  private static Predicate genereratePredicate(UserSearchRequest userSearchRequest) {
    if (log.isDebugEnabled()) {
      log.debug("createSort {}", userSearchRequest);
    }
    final var andBuilder = new BooleanBuilder();
    QUserEntity qUserEntity = QUserEntity.userEntity;
    if (StringUtils.isNotEmpty(userSearchRequest.getSearchTerm())) {
      final var orBuilder = new BooleanBuilder();
      orBuilder
          .or(qUserEntity.username.contains(userSearchRequest.getSearchTerm()))
          .or(qUserEntity.fullName.contains(userSearchRequest.getSearchTerm()));
      andBuilder.and(orBuilder);
    }
    if (Objects.nonNull(userSearchRequest.getIsAdmin()) && BooleanUtils.isTrue(
        userSearchRequest.getIsAdmin())) {
      andBuilder.and(qUserEntity.roleEntities.any().name.eq(RoleEnum.ROLE_ADMIN.name()));
    }
    if (StringUtils.isNotEmpty(userSearchRequest.getUsername())) {
      andBuilder.and(qUserEntity.username.eq(userSearchRequest.getUsername()));
    }
    if (StringUtils.isNotEmpty(userSearchRequest.getFullName())) {
      andBuilder.and(qUserEntity.fullName.eq(userSearchRequest.getFullName()));
    }
    return andBuilder;
  }

  @Override
  public UserDto changeAdminStatus(Long id, Boolean adminFlag) {
    if (log.isDebugEnabled()) {
      log.debug("changeAdminStatus {} {}", id, adminFlag);
    }
    var optionalUserEntity = userService.getUserById(id);
    if (optionalUserEntity.isPresent()) {
      var entity = optionalUserEntity.get();
      return changeAdminStatus(entity, adminFlag);

    }
    throw new CustomResponseStatusException(HttpStatus.NOT_FOUND, ErrorCodeEnum.USER_NOT_FOUND);
  }

  public UserDto changeAdminStatus(UserEntity entity, Boolean setToAdminFlag) {
    if (log.isDebugEnabled()) {
      log.debug("changeAdminStatus {} {}", entity.getSk(), setToAdminFlag);
    }
    if (Objects.nonNull(setToAdminFlag)) {
      if (BooleanUtils.isTrue(setToAdminFlag) && !RoleUtil.hasAdminRole(entity)) {
        entity.getRoleEntities().add(roleService.findByName(RoleEnum.ROLE_ADMIN.name()));
      } else if (BooleanUtils.isFalse(setToAdminFlag) && RoleUtil.hasAdminRole(entity)) {
        removeAdminRole(entity);
      }
    }
    return mapper.mapToUserDto(userService.save(entity));
  }

  private void removeAdminRole(UserEntity entity) {
    if (log.isDebugEnabled()) {
      log.debug("removeAdminRole {}", entity.getSk());
    }
    var filteredRoles = entity.getRoleEntities().stream()
        .filter(roleEntity -> !RoleEnum.ROLE_ADMIN.name().equalsIgnoreCase(roleEntity.getName()))
        .collect(
            Collectors.toSet());
    entity.getRoleEntities().clear();
    filteredRoles.forEach(entity::addRole);
    if (CollectionUtils.isEmpty(entity.getRoleEntities())) {
      entity.addRole(roleService.findByName(RoleEnum.ROLE_USER.name()));
    }
  }

  @Override
  public UserDto getUser(Long userId) {
    if (log.isDebugEnabled()) {
      log.debug("getUser {}", userId);
    }
    var optionalUserEntity = userService.getUserById(userId);
    if (optionalUserEntity.isPresent()) {
      return mapper.mapToUserDto(optionalUserEntity.get());
    }
    throw new CustomResponseStatusException(HttpStatus.NOT_FOUND, ErrorCodeEnum.USER_NOT_FOUND);
  }
}
