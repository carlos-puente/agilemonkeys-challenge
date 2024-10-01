package me.carlosjai.agilemonkeyschallenge.api.auth.controller;

import lombok.AllArgsConstructor;
import me.carlosjai.agilemonkeyschallenge.api.user.dto.JwtResponse;
import me.carlosjai.agilemonkeyschallenge.auth.dto.LoginUserDto;
import me.carlosjai.agilemonkeyschallenge.auth.dto.RegisterUserDto;
import me.carlosjai.agilemonkeyschallenge.auth.jwt.service.JwtService;
import me.carlosjai.agilemonkeyschallenge.auth.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
@AllArgsConstructor
public class AuthenticationController {

  private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);

  private final JwtService jwtService;

  private final AuthenticationService authenticationService;

  /**
   * Handles the user signup process by creating a new user.
   * <p>
   * This endpoint listens for HTTP POST requests at the "/signup" path. It accepts a
   * {@link RegisterUserDto} object in the request body to capture the user's registration details.
   * The user is created by invoking the {@code signup} method of the
   * {@code AuthenticationService}.
   *
   * @param registerUserDto the registration details of the new user.
   * @return a {@link ResponseEntity} containing a success message and the username of the newly
   * created user.
   */
  @PostMapping("/signup")
  public ResponseEntity<String> register(@RequestBody RegisterUserDto registerUserDto) {
    if (log.isDebugEnabled()) {
      log.debug("register: {}", registerUserDto);
    }
    var newUser = authenticationService.signup(registerUserDto);

    return ResponseEntity.ok("User created: " + newUser.getUsername());
  }

  /**
   * Handles user authentication and returns a JWT upon successful login.
   * <p>
   * This endpoint listens for HTTP POST requests at the "/login" path. It accepts a
   * {@link LoginUserDto} object in the request body to capture the user's login credentials. The
   * user's credentials are validated by invoking the {@code authenticate} method of the
   * {@code AuthenticationService}. If authentication is successful, a JWT is generated using the
   * {@code JwtService} and returned in the response.
   *
   * @param loginUserDto the login credentials of the user (e.g., username and password).
   * @return a {@link ResponseEntity} containing a {@link JwtResponse} with the generated JWT token
   * and its expiration time.
   */
  @PostMapping("/login")
  public ResponseEntity<JwtResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
    if (log.isDebugEnabled()) {
      log.debug("authenticate: {}", loginUserDto);
    }
    var authenticatedUser = authenticationService.authenticate(loginUserDto);

    String jwtToken = jwtService.generateToken(authenticatedUser);

    JwtResponse loginResponse = JwtResponse.builder().accessToken(jwtToken)
        .expiresIn(jwtService.getExpirationTime()).build();

    return ResponseEntity.ok(loginResponse);
  }
}