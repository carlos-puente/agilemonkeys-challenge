package auth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.carlosjai.agilemonkeyschallenge.api.auth.controller.AuthenticationController;
import me.carlosjai.agilemonkeyschallenge.auth.dto.CustomUserDetails;
import me.carlosjai.agilemonkeyschallenge.auth.dto.LoginUserDto;
import me.carlosjai.agilemonkeyschallenge.auth.dto.RegisterUserDto;
import me.carlosjai.agilemonkeyschallenge.auth.jwt.service.JwtService;
import me.carlosjai.agilemonkeyschallenge.auth.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class AuthenticationControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Mock
  private JwtService jwtService;

  @Mock
  private AuthenticationService authenticationService;

  @InjectMocks
  private AuthenticationController authenticationController;

  private ObjectMapper objectMapper;

  @BeforeEach
  public void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
    objectMapper = new ObjectMapper();
  }

  @Test
  public void testRegister_Success() throws Exception {

    RegisterUserDto registerUserDto = RegisterUserDto.builder().username("testUser")
        .fullName("testUser").password("TestPassw!ord123").build();
    CustomUserDetails newUser = new CustomUserDetails(
        me.carlosjai.agilemonkeyschallenge.api.data.DataGenerator.generateUserEntity(1L));
    newUser.setUsername("testUser");

    when(authenticationService.signup(any(RegisterUserDto.class))).thenReturn(newUser);

    mockMvc.perform(post("/auth/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registerUserDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").value("User created: testUser"));
  }

  @Test
  public void testAuthenticate_Success() throws Exception {

    LoginUserDto loginUserDto = LoginUserDto.builder().username("testUser")
        .password("testPassword!2344aBd").build();
    CustomUserDetails authenticatedUser = new CustomUserDetails(
        me.carlosjai.agilemonkeyschallenge.api.data.DataGenerator.generateUserEntity(1L));
    authenticatedUser.setUsername("testUser");
    String jwtToken = "mockJwtToken";

    when(authenticationService.authenticate(any(LoginUserDto.class))).thenReturn(authenticatedUser);
    when(jwtService.generateToken(authenticatedUser)).thenReturn(jwtToken);
    when(jwtService.getExpirationTime()).thenReturn(3600L);  // Mock expiration time

    mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginUserDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.accessToken").value(jwtToken))
        .andExpect(jsonPath("$.expiresIn").value(3600));
  }
}
