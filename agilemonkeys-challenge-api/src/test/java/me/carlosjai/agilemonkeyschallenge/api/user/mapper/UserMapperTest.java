package me.carlosjai.agilemonkeyschallenge.api.user.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.util.AssertionErrors.assertTrue;

import java.time.LocalDateTime;
import me.carlosjai.agilemonkeyschallenge.api.user.dto.UserDto;
import me.carlosjai.agilemonkeyschallenge.domain.user.model.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserMapperTest {

  private UserMapperImpl userMapper;

  @BeforeEach
  public void setUp() {
    userMapper = new UserMapperImpl();
  }

  @Test
  public void testMapToUserDto_NullEntity() {
    // Test for null entity
    UserDto result = userMapper.mapToUserDto(null);
    assertNull(result, "Mapping null entity should return null");
  }

  @Test
  public void testMapToUserDto_CompleteEntity() {
    UserEntity entity = me.carlosjai.agilemonkeyschallenge.api.data.DataGenerator.generateUserEntity(
        1L);
    UserDto result = userMapper.mapToUserDto(entity);

    assertNotNull(result, "Mapping should return a non-null UserDto");
    assertEquals(entity.getSk(), result.getId(), "IDs should match");
    assertEquals(entity.getUsername(), result.getUsername(), "Usernames should match");
    assertEquals(entity.getFullName(), result.getFullName(), "Full names should match");
    assertEquals(entity.getCreatedBy(), result.getCreatedBy(), "Created by should match");
    assertEquals(entity.getRoleEntities().size(), result.getRoles().size(),
        "Roles size should match");
    assertEquals(LocalDateTime.of(2022, 1, 1, 12, 0).toLocalDate(), result.getCreationDate(),
        "Creation dates should match");
    assertEquals(entity.getLastModifiedBy(), result.getLastModifiedBy(),
        "Last modified by should match");
    assertEquals(LocalDateTime.of(2023, 1, 1, 12, 0).toLocalDate(), result.getLastModifiedDate(),
        "Last modified dates should match");
  }

  @Test
  public void testMapToUserDto_EntityWithNullFields() {

    UserEntity entity = me.carlosjai.agilemonkeyschallenge.api.data.DataGenerator.generateUserEntity(
        1L);
    entity.setUsername(null);
    entity.setCreationDate(null);
    entity.setRoleEntities(null);
    UserDto result = userMapper.mapToUserDto(entity);

    assertNotNull(result, "Mapping should return a non-null UserDto");
    assertEquals(entity.getSk(), result.getId(), "IDs should match");
    assertNull(result.getUsername(), "Username should be null");
    assertTrue("Roles should be empty", result.getRoles().isEmpty());
    assertEquals(entity.getFullName(), result.getFullName(), "Full names should match");
    assertEquals(entity.getCreatedBy(), result.getCreatedBy(), "Created by should match");
    assertNull(result.getCreationDate(), "Creation date should be null");
    assertEquals(entity.getLastModifiedBy(), result.getLastModifiedBy(),
        "Last modified by should match");
    assertEquals(LocalDateTime.of(2023, 1, 1, 12, 0).toLocalDate(), result.getLastModifiedDate(),
        "Last modified dates should match");
  }
}
