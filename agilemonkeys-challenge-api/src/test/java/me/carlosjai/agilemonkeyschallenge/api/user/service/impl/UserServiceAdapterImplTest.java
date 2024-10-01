package me.carlosjai.agilemonkeyschallenge.api.user.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;
import me.carlosjai.agilemonkeyschallenge.api.user.dto.UserDto;
import me.carlosjai.agilemonkeyschallenge.api.user.mapper.UserMapper;
import me.carlosjai.agilemonkeyschallenge.api.user.request.UserCreateRequest;
import me.carlosjai.agilemonkeyschallenge.api.user.request.UserUpdateRequest;
import me.carlosjai.agilemonkeyschallenge.domain.exception.CustomResponseStatusException;
import me.carlosjai.agilemonkeyschallenge.domain.user.model.entity.RoleEntity;
import me.carlosjai.agilemonkeyschallenge.domain.user.model.entity.UserEntity;
import me.carlosjai.agilemonkeyschallenge.domain.user.model.service.RoleService;
import me.carlosjai.agilemonkeyschallenge.domain.user.model.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceAdapterImplTest {

  @Mock
  private UserService userService;

  @Mock
  private RoleService roleService;

  @Mock
  private UserMapper mapper;

  @InjectMocks
  private UserServiceAdapterImpl userServiceAdapter;

  @Mock
  private PasswordEncoder encoder;

  private UserEntity userEntity;
  private UserDto userDto;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    userEntity = me.carlosjai.agilemonkeyschallenge.api.data.DataGenerator.generateUserEntity(1L);
    userDto = UserDto.builder().id(1L).username("testuser").build();
  }

  @Test
  void testCreateUser() {
    UserCreateRequest createRequest = new UserCreateRequest();
    createRequest.setUsername("testuser");
    createRequest.setPassword("password");
    createRequest.setFullname("Test User");
    createRequest.setRoles(Set.of("ROLE_USER"));

    when(userService.findByUsername("testuser")).thenReturn(Optional.empty());
    when(roleService.findByName("ROLE_USER")).thenReturn(new RoleEntity());
    when(userService.save(any(UserEntity.class))).thenReturn(userEntity);
    when(mapper.mapToUserDto(any(UserEntity.class))).thenReturn(userDto);
    when(encoder.encode(any())).thenReturn("testEncodedThing");

    UserDto result = userServiceAdapter.createUser(createRequest);

    assertNotNull(result);
    assertEquals("testuser", result.getUsername());
    verify(userService).findByUsername("testuser");
    verify(userService).save(any(UserEntity.class));
  }

  @Test
  void testCreateUser_UsernameConflict() {
    UserCreateRequest createRequest = new UserCreateRequest();
    createRequest.setUsername("testuser");

    when(userService.findByUsername("testuser")).thenReturn(Optional.of(userEntity));

    CustomResponseStatusException exception = assertThrows(CustomResponseStatusException.class,
        () ->
            userServiceAdapter.createUser(createRequest));

    assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
    assertEquals("The specified username is already being used.", exception.getReason());
  }

  @Test
  void testUpdateUser() {
    UserUpdateRequest updateRequest = new UserUpdateRequest();
    updateRequest.setId(1L);
    updateRequest.setFullname("Updated User");
    updateRequest.setRoles(Set.of("ROLE_ADMIN"));

    when(userService.getUserById(1L)).thenReturn(Optional.of(userEntity));
    when(roleService.findByName("ROLE_ADMIN")).thenReturn(new RoleEntity());
    when(userService.save(any(UserEntity.class))).thenReturn(userEntity);
    when(mapper.mapToUserDto(any(UserEntity.class))).thenReturn(userDto);

    UserDto result = userServiceAdapter.updateUser(updateRequest);

    assertNotNull(result);
    assertEquals("testuser", result.getUsername());
    verify(userService).getUserById(1L);
    verify(userService).save(any(UserEntity.class));
  }

  @Test
  void testUpdateUser_NotFound() {
    UserUpdateRequest updateRequest = new UserUpdateRequest();
    updateRequest.setId(1L);

    when(userService.getUserById(1L)).thenReturn(Optional.empty());

    CustomResponseStatusException exception = assertThrows(CustomResponseStatusException.class,
        () ->
            userServiceAdapter.updateUser(updateRequest));

    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    assertEquals("User not found with the given id.", exception.getReason());
  }

  @Test
  void testDeleteUser() {
    when(userService.getUserById(1L)).thenReturn(Optional.of(userEntity));

    userServiceAdapter.deleteUser(1L);

    verify(userService).delete(userEntity);
  }

  @Test
  void testDeleteUser_NotFound() {
    when(userService.getUserById(1L)).thenReturn(Optional.empty());

    CustomResponseStatusException exception = assertThrows(CustomResponseStatusException.class,
        () ->
            userServiceAdapter.deleteUser(1L));

    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    assertEquals("User not found with the given id.", exception.getReason());
  }

  @Test
  void testGetUser() {
    when(userService.getUserById(1L)).thenReturn(Optional.of(userEntity));
    when(mapper.mapToUserDto(userEntity)).thenReturn(userDto);

    UserDto result = userServiceAdapter.getUser(1L);

    assertNotNull(result);
    assertEquals("testuser", result.getUsername());
    verify(userService).getUserById(1L);
  }

  @Test
  void testGetUser_NotFound() {
    when(userService.getUserById(1L)).thenReturn(Optional.empty());

    CustomResponseStatusException exception = assertThrows(CustomResponseStatusException.class,
        () ->
            userServiceAdapter.getUser(1L));

    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    assertEquals("User not found with the given id.", exception.getReason());
  }

}
