package me.carlosjai.agilemonkeyschallenge.auth.userdetail;

import java.util.Optional;
import me.carlosjai.agilemonkeyschallenge.auth.dto.CustomUserDetails;
import me.carlosjai.agilemonkeyschallenge.domain.user.model.entity.UserEntity;
import me.carlosjai.agilemonkeyschallenge.domain.user.model.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

  /**
   * Loads a user by their username from the database.
   * <p>
   * This method retrieves a user entity based on the provided username. If the user is found, it
   * returns a {@link CustomUserDetails} object containing the user's details. If the user cannot be
   * found, a {@link UsernameNotFoundException} is thrown.
   *
   * @param username the username of the user to be loaded, must not be null or empty.
   * @return a {@link UserDetails} object representing the authenticated user.
   * @throws UsernameNotFoundException if the user with the given username cannot be found.
   */
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    logger.debug("loadUserByUsername('{}')", username);
    Optional<UserEntity> optUser = userRepository.findByUsernameIgnoreCase(username);
    if (optUser.isEmpty()) {
      logger.error("Username not found: {}", username);
      throw new UsernameNotFoundException("could not found user '" + username + "'");
    }
    logger.debug("User '{}' authenticated Successfully.", username);
    return new CustomUserDetails(optUser.get());
  }
}
