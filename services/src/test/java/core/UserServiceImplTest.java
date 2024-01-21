package core;

import core.model.dto.UserDto;
import core.model.dto.UserShortDto;
import core.model.entity.Role;
import core.model.entity.User;
import core.model.repository.UserRepository;
import core.service.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private List<User> users;

    @Before
    public void setup() {
        users = new ArrayList<>();
        User userOne = new User();
        userOne.setId(1);
        userOne.setName("John");
        userOne.setSurname("Doe");
        userOne.setEmail("john@example.com");
        userOne.setRoles(Arrays.asList(Role.ROLE_SALE_USER));

        User userTwo = new User();
        userTwo.setId(2);
        userTwo.setName("Jane");
        userTwo.setSurname("Smith");
        userTwo.setEmail("jane@example.com");
        userTwo.setRoles(Arrays.asList(Role.ROLE_SALE_USER));

        users.add(userOne);
        users.add(userTwo);

        users.sort(Comparator.comparing(User::getEmail));
        when(userRepository.findAll()).thenReturn(users);
    }

    @Test
    public void testAddUser() {
        UserDto userDto = new UserDto();
        userDto.setName("John");
        userDto.setSurname("Doe");
        userDto.setPatronymic("Marcus");
        userDto.setEmail("john@example.com");

        User expectedUser = new User();
        expectedUser.setName("John");
        expectedUser.setSurname("Doe");
        expectedUser.setPatronymic("Marcus");
        expectedUser.setEmail("john@example.com");
        expectedUser.setRoles(Arrays.asList(Role.ROLE_SALE_USER));

        User addedUser = userService.addUser(userDto);

        assertEquals(expectedUser, addedUser);
    }

    @Test
    public void testGetAll() {
        int pageNum = 0;
        int pageSize = 10;
        List<User> users = new ArrayList<>();
        User userTwo = new User();
        userTwo.setSurname("Smith");
        userTwo.setName("Jane");
        userTwo.setPatronymic("Gerda");
        userTwo.setEmail("jane.smith@example.com");
        userTwo.setRoles(Arrays.asList(Role.ROLE_SALE_USER));
        User userOne = new User();
        userOne.setSurname("Smith");
        userOne.setName("John");
        userOne.setPatronymic("Gerhard");
        userOne.setEmail("john.Smith@example.com");
        userOne.setRoles(Arrays.asList(Role.ROLE_SALE_USER));
        users.add(userTwo);
        users.add(userOne);
        users.sort(Comparator.comparing(User::getEmail));
        Page<User> usersPage = new PageImpl<>(users);
        when(userRepository.findAll(any(Pageable.class))).thenReturn(usersPage);
        List<UserShortDto> expectedUserDtoList = new ArrayList<>();
        for (User user : users) {
            UserShortDto userShortDto = new UserShortDto();
            String fullName = user.getSurname() + " " + user.getName() + " " + user.getPatronymic();
            userShortDto.setFullName(fullName);
            userShortDto.setEmail(user.getEmail());
            userShortDto.setRoles(Arrays.asList(Role.ROLE_SALE_USER));
            expectedUserDtoList.add(userShortDto);
        }
        List<UserShortDto> actualUserDtoList = userService.getAll(pageNum, pageSize);
        assertNotNull(actualUserDtoList);
        assertEquals(expectedUserDtoList, actualUserDtoList);
        verify(userRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    public void testSaveUser() {
        User user = new User();
        user.setId(1);
        user.setName("John");
        user.setSurname("Doe");
        user.setEmail("john@example.com");
        user.setRoles(Arrays.asList(Role.ROLE_CUSTOMER_USER));
        when(userRepository.save(user)).thenReturn(user);
        boolean isSaved = userService.saveUser(user);
        assertEquals(true, isSaved);
        verify(userRepository, times(1)).save(user);
    }
}
