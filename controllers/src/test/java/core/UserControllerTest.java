package core;

import core.controller.UserController;
import core.model.dto.UserDto;
import core.model.dto.UserShortDto;
import core.model.entity.Role;
import core.model.entity.User;
import core.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Before
    public void setup() {
        mockMvc = standaloneSetup(userController).build();
    }

    @Test
    public void testAddUser() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setSurname("Smith");
        userDto.setName("John");
        userDto.setEmail("john.smith@example.com");
        User user = new User();
        user.setId(1);
        user.setSurname("Smith");
        user.setName("John");
        user.setEmail("john.smith@example.com");
        when(userService.addUser(any(UserDto.class))).thenReturn(user);
        when(userService.saveUser(any(User.class))).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.post("/addUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"surname\":\"Smith\",\"name\":\"John\",\"email\":\"john.smith@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("User added successfully"));
    }

    @Test
    public void testAddUser_InvalidInput() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/addUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John\",\"email\":\"john.smith@example.com\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetAll() throws Exception {
        List<UserShortDto> userShortDtoList = new ArrayList<>();
        UserShortDto userDtoOne = new UserShortDto();
        userDtoOne.setFullName("Parker Jonatan Reez");
        userDtoOne.setEmail("pjr@gmail.com");
        userDtoOne.setRoles(Arrays.asList(Role.ROLE_CUSTOMER_USER));
        UserShortDto userDtoTwo = new UserShortDto();
        userDtoTwo.setFullName("Forester Antuan Ray");
        userDtoTwo.setEmail("far@gmail.com");
        userDtoTwo.setRoles(Arrays.asList(Role.ROLE_CUSTOMER_USER));
        userShortDtoList.add(userDtoOne);
        userShortDtoList.add(userDtoTwo);
        when(userService.getAll(anyInt(), anyInt())).thenReturn(userShortDtoList);
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json("["
                        + "{"
                        + "\"fullName\":\"Parker Jonatan Reez\","
                        + "\"email\":\"pjr@gmail.com\","
                        + "\"roles\":[\"ROLE_CUSTOMER_USER\"]"
                        + "},"
                        + "{"
                        + "\"fullName\":\"Forester Antuan Ray\","
                        + "\"email\":\"far@gmail.com\","
                        + "\"roles\":[\"ROLE_CUSTOMER_USER\"]"
                        + "}"
                        + "]"
                ));
        verify(userService, times(1)).getAll(anyInt(), anyInt());
    }
}
