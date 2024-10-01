package me.carlosjai.agilemonkeyschallenge.api.user.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import me.carlosjai.agilemonkeyschallenge.api.base.response.BasePageResponse;
import me.carlosjai.agilemonkeyschallenge.api.user.dto.UserDto;
import me.carlosjai.agilemonkeyschallenge.api.user.request.UserCreateRequest;
import me.carlosjai.agilemonkeyschallenge.api.user.request.UserSearchRequest;
import me.carlosjai.agilemonkeyschallenge.api.user.request.UserUpdateRequest;
import me.carlosjai.agilemonkeyschallenge.api.user.service.UserServiceAdapter;
import me.carlosjai.agilemonkeyschallenge.domain.constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * UserController is a Spring MVC controller responsible for handling HTTP requests related to user
 * management. It provides endpoints for creating, retrieving, updating, and deleting users in the
 * system.
 *
 * <p>This controller is annotated with {@link org.springframework.web.bind.annotation.Controller}
 * and is mapped to the user-related endpoints defined under the API version specified by
 * {@link Constants#API} and {@link Constants#V1}.
 *
 * <p>Access to the methods in this controller is restricted to users with
 * the 'ADMIN' role, enforced by the {@link PreAuthorize} annotation.
 *
 * <p>Endpoints include:
 * <ul>
 *   <li>{@code POST /create}: Creates a new user based on the provided
 *   {@link UserCreateRequest}.</li>
 *   <li>{@code DELETE /{id}/delete}: Deletes the user identified by
 *   the specified ID.</li>
 *   <li>{@code GET /{id}}: Retrieves the details of the user identified
 *   by the specified ID.</li>
 *   <li>{@code PUT /update}: Updates the details of an existing user
 *   based on the provided {@link UserUpdateRequest}.</li>
 *   <li>{@code POST}: Lists users based on the specified
 *   {@link UserSearchRequest} criteria.</li>
 *   <li>{@code PATCH /{id}/update/admin-status}: Changes the admin status
 *   of a user identified by the specified ID.</li>
 * </ul>
 */
@RequestMapping(Constants.API + Constants.V1 + "/user")
@Controller
@AllArgsConstructor
public class UserController {

  private static final Logger log = LoggerFactory.getLogger(UserController.class);

  private final UserServiceAdapter userServiceAdapter;

  /**
   * Retrieves the details of a user identified by the specified ID.
   *
   * @param id the unique identifier of the user whose details are to be retrieved
   * @return a {@link ResponseEntity} containing the user data
   */
  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/{id}")
  public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
    if (log.isDebugEnabled()) {
      log.debug("getUser {}", id);
    }
    return ResponseEntity.ok(userServiceAdapter.getUser(id));
  }

  /**
   * Lists users based on the specified search criteria.
   *
   * @param userSearchRequest the criteria for searching users
   * @return a {@link ResponseEntity} containing a paginated list of users
   */
  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping
  public ResponseEntity<BasePageResponse<UserDto>> listUsers(
      @RequestBody UserSearchRequest userSearchRequest) {

    if (log.isDebugEnabled()) {
      log.debug("listUsers {}", userSearchRequest);
    }
    return ResponseEntity.ok(userServiceAdapter.listUsers(userSearchRequest));
  }


  /**
   * Creates a new user in the system based on the provided user creation request.
   *
   * @param userCreateRequest the request containing user details for the new user
   * @return a {@link ResponseEntity} containing the created user data with a status of
   * {@link HttpStatus#CREATED}
   */
  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/create")
  public ResponseEntity<UserDto> createUser(
      @Valid @RequestBody UserCreateRequest userCreateRequest) {
    if (log.isDebugEnabled()) {
      log.debug("createUser {}", userCreateRequest.getUsername());
    }
    UserDto createdUser = userServiceAdapter.createUser(userCreateRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
  }

  /**
   * Updates the details of an existing user based on the provided update request.
   *
   * @param updateRequest the request containing updated user details
   * @return a {@link ResponseEntity} containing the updated user data
   */
  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("/update")
  public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserUpdateRequest updateRequest) {
    if (log.isDebugEnabled()) {
      log.debug("updateUser {}", updateRequest.getId());
    }
    UserDto updatedUser = userServiceAdapter.updateUser(updateRequest);
    return ResponseEntity.ok(updatedUser);
  }

  /**
   * Changes the admin status of a user identified by the specified ID.
   *
   * @param id      the unique identifier of the user whose admin status is to be changed
   * @param isAdmin the new admin status to set for the user
   * @return a {@link ResponseEntity} containing the updated user data
   */
  @PreAuthorize("hasRole('ADMIN')")
  @PatchMapping("/{id}/update/admin-status")
  public ResponseEntity<UserDto> changeAdminStatus(@PathVariable Long id,
      @RequestParam Boolean isAdmin) {
    if (log.isDebugEnabled()) {
      log.debug("changeAdminStatus {} {}", id, isAdmin);
    }
    UserDto updatedUser = userServiceAdapter.changeAdminStatus(id, isAdmin);
    return ResponseEntity.ok(updatedUser);
  }

  /**
   * Deletes a user identified by the specified ID from the system.
   *
   * @param id the unique identifier of the user to be deleted
   * @return a {@link ResponseEntity} with no content and a status of {@link HttpStatus#NO_CONTENT}
   */
  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/{id}/delete")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    if (log.isDebugEnabled()) {
      log.debug("deleteUser {}", id);
    }
    userServiceAdapter.deleteUser(id);
    return ResponseEntity.noContent().build();
  }
}
