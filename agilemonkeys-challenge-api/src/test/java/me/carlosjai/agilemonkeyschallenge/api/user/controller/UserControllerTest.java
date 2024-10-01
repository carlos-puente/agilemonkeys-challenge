import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.List;
import me.carlosjai.agilemonkeyschallenge.api.base.response.BasePageResponse;
import me.carlosjai.agilemonkeyschallenge.api.user.controller.UserController;
import me.carlosjai.agilemonkeyschallenge.api.user.dto.UserDto;
import me.carlosjai.agilemonkeyschallenge.api.user.request.UserCreateRequest;
import me.carlosjai.agilemonkeyschallenge.api.user.request.UserSearchRequest;
import me.carlosjai.agilemonkeyschallenge.api.user.request.UserUpdateRequest;
import me.carlosjai.agilemonkeyschallenge.api.user.service.UserServiceAdapter;
import me.carlosjai.agilemonkeyschallenge.domain.constants.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Mock
  private UserServiceAdapter userServiceAdapter;

  @InjectMocks
  private UserController userController;

  private ObjectMapper objectMapper;

  @BeforeEach
  public void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    objectMapper = new ObjectMapper();
  }

  @Test
  @WithMockUser(roles = "ADMIN") // Assuming only admins can access this endpoint
  public void testCreateUser() throws Exception {
    UserCreateRequest request = new UserCreateRequest();
    request.setFullname("test");
    request.setPassword("TEST!admin123");
    request.setUsername("testUser");
    UserDto createdUser = new UserDto(1L, "testUser", "Test User", "test", LocalDate.now(), "test",
        LocalDate.now());

    when(userServiceAdapter.createUser(any(UserCreateRequest.class))).thenReturn(createdUser);

    mockMvc.perform(post(Constants.API + Constants.V1 + "/user/create")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.username").value("testUser"))
        .andExpect(jsonPath("$.fullName").value("Test User"));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void testDeleteUser() throws Exception {
    Long userId = 1L;

    doNothing().when(userServiceAdapter).deleteUser(userId);

    mockMvc.perform(delete(Constants.API + Constants.V1 + "/user/{id}/delete", userId))
        .andExpect(status().isNoContent());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void testGetUser() throws Exception {
    Long userId = 1L;
    UserDto userDto = new UserDto(1L, "testUser", "Test User", "test", LocalDate.now(), "test",
        LocalDate.now());

    when(userServiceAdapter.getUser(userId)).thenReturn(userDto);

    mockMvc.perform(get(Constants.API + Constants.V1 + "/user/{id}", userId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(userId))
        .andExpect(jsonPath("$.username").value("testUser"))
        .andExpect(jsonPath("$.fullName").value("Test User"));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void testUpdateUser() throws Exception {
    UserUpdateRequest updateRequest = new UserUpdateRequest();
    updateRequest.setId(1L);
    UserDto updatedUser = new UserDto(1L, "testUser", "Test User", "test", LocalDate.now(), "test",
        LocalDate.now());

    when(userServiceAdapter.updateUser(any(UserUpdateRequest.class))).thenReturn(updatedUser);

    mockMvc.perform(put(Constants.API + Constants.V1 + "/user/update")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.username").value("testUser"))
        .andExpect(jsonPath("$.fullName").value("Test User"));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void testListUsers() throws Exception {
    UserSearchRequest searchRequest = new UserSearchRequest();
    BasePageResponse<UserDto> response = new BasePageResponse<>();
    response.setContent(
        List.of(new UserDto(1L, "testUser", "Test User", "test", LocalDate.now(), "test",
            LocalDate.now())));

    when(userServiceAdapter.listUsers(any(UserSearchRequest.class))).thenReturn(response);

    mockMvc.perform(post(Constants.API + Constants.V1 + "/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(searchRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].id").value(1L))
        .andExpect(jsonPath("$.content[0].username").value("testUser"))
        .andExpect(jsonPath("$.content[0].fullName").value("Test User"));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void testChangeAdminStatus() throws Exception {
    Long userId = 1L;
    Boolean newAdminStatus = true;
    UserDto updatedUser = new UserDto(1L, "testUser", "Test User", "test", LocalDate.now(), "test",
        LocalDate.now());

    when(userServiceAdapter.changeAdminStatus(userId, newAdminStatus)).thenReturn(updatedUser);

    mockMvc.perform(patch(Constants.API + Constants.V1 + "/user/{id}/update/admin-status", userId)
            .param("isAdmin", String.valueOf(newAdminStatus)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(userId))
        .andExpect(jsonPath("$.username").value("testUser"))
        .andExpect(jsonPath("$.fullName").value("Test User"));
  }
}
