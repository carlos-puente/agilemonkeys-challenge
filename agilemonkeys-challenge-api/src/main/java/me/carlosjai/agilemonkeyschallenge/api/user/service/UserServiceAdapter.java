package me.carlosjai.agilemonkeyschallenge.api.user.service;

import me.carlosjai.agilemonkeyschallenge.api.base.response.BasePageResponse;
import me.carlosjai.agilemonkeyschallenge.api.user.dto.UserDto;
import me.carlosjai.agilemonkeyschallenge.api.user.request.UserCreateRequest;
import me.carlosjai.agilemonkeyschallenge.api.user.request.UserSearchRequest;
import me.carlosjai.agilemonkeyschallenge.api.user.request.UserUpdateRequest;

public interface UserServiceAdapter {

  UserDto createUser(UserCreateRequest userCreateRequest);

  void deleteUser(Long userId);

  UserDto updateUser(UserUpdateRequest updateRequest);

  BasePageResponse<UserDto> listUsers(UserSearchRequest userSearchRequest);

  UserDto changeAdminStatus(Long userId, Boolean isAdmin);

  UserDto getUser(Long id);
}
