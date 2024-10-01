package me.carlosjai.agilemonkeyschallenge.auth.service;


import java.time.Instant;
import java.util.Date;
import lombok.AllArgsConstructor;
import me.carlosjai.agilemonkeyschallenge.auth.dto.CustomUserDetails;
import me.carlosjai.agilemonkeyschallenge.auth.dto.LoginUserDto;
import me.carlosjai.agilemonkeyschallenge.auth.dto.RegisterUserDto;
import me.carlosjai.agilemonkeyschallenge.domain.exception.CustomResponseStatusException;
import me.carlosjai.agilemonkeyschallenge.domain.exception.ErrorCodeEnum;
import me.carlosjai.agilemonkeyschallenge.domain.user.definition.RoleEnum;
import me.carlosjai.agilemonkeyschallenge.domain.user.model.entity.UserEntity;
import me.carlosjai.agilemonkeyschallenge.domain.user.model.repository.RoleRepository;
import me.carlosjai.agilemonkeyschallenge.domain.user.model.repository.UserRepository;
import me.carlosjai.agilemonkeyschallenge.domain.util.PasswordUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;


  public CustomUserDetails signup(RegisterUserDto input) {
    var existingEntity = userRepository.findByUsernameIgnoreCase(input.getUsername());
    if (existingEntity.isPresent()) {
      throw new CustomResponseStatusException(HttpStatus.CONFLICT,
          ErrorCodeEnum.AUTH_SIGNUP_EXISTING_USER);
    }
    PasswordUtil.validatePassword(input.getPassword());
    UserEntity userEntity = new UserEntity();
    userEntity.setUsername(input.getUsername());
    userEntity.setPassword(passwordEncoder.encode(input.getPassword()));
    userEntity.setFullName(input.getFullName());
    userEntity.addRole(roleRepository.findByNameIgnoreCase(RoleEnum.ROLE_USER.name()));
    userEntity.setCreatedBy("SIGNUP PROCESS");
    userEntity.setCreationDate(Date.from(Instant.now()));
    return new CustomUserDetails(userRepository.save(userEntity));
  }

  public CustomUserDetails authenticate(LoginUserDto input) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            input.getUsername(),
            input.getPassword()
        )
    );

    return new CustomUserDetails(userRepository.findByUsernameIgnoreCase(input.getUsername())
        .orElseThrow());
  }
}
