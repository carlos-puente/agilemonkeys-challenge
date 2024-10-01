package me.carlosjai.agilemonkeyschallenge.auth.userdetail;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import me.carlosjai.agilemonkeyschallenge.domain.user.model.entity.UserEntity;
import me.carlosjai.agilemonkeyschallenge.domain.user.model.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class UserDetailsServiceImplTest {

  @InjectMocks
  private UserDetailsServiceImpl userDetailsService;

  @Mock
  private UserRepository userRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void loadUserByUsername_shouldReturnUserDetails_whenUserExists() {
    String username = "testuser";
    UserEntity userEntity = new UserEntity();
    userEntity.setUsername(username);
    when(userRepository.findByUsernameIgnoreCase(username)).thenReturn(Optional.of(userEntity));
    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
    assertNotNull(userDetails);
    assertEquals(username, userDetails.getUsername());
    verify(userRepository).findByUsernameIgnoreCase(username);
  }

  @Test
  void loadUserByUsername_shouldThrowUsernameNotFoundException_whenUserDoesNotExist() {
    String username = "nonexistentuser";
    when(userRepository.findByUsernameIgnoreCase(username)).thenReturn(Optional.empty());
    UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
      userDetailsService.loadUserByUsername(username);
    });
    assertEquals("could not found user 'nonexistentuser'", exception.getMessage());
    verify(userRepository).findByUsernameIgnoreCase(username);
  }
}