package core;

import core.model.entity.User;
import core.model.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = UserRepository.class)
public class UserRepositoryTest {
    @Mock
    private UserRepository userRepository;

    @Test
    void testSaveUser() {
        User user = new User();
        user.setId(1);
        user.setEmail("test@example.com");
        user.setName("John");
        user.setSurname("Doe");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        userRepository.save(user);
        User retrievedUser = userRepository.findById(user.getId()).orElse(null);
        assertNotNull(retrievedUser);
        assertEquals(user.getEmail(), retrievedUser.getEmail());
        assertEquals(user.getName(), retrievedUser.getName());
        assertEquals(user.getSurname(), retrievedUser.getSurname());
    }

    @Test
    void testFindAll() {
        List<User> mockUsers = new ArrayList<>();
        when(userRepository.findAll()).thenReturn(mockUsers);
        User user1 = new User();
        user1.setEmail("test@example.com");
        user1.setName("John");
        user1.setSurname("Doe");
        User user2 = new User();
        user2.setEmail("test@example.com");
        user2.setName("John");
        user2.setSurname("Doe");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            mockUsers.add(savedUser);
            return savedUser;
        });
        userRepository.save(user1);
        userRepository.save(user2);
        List<User> retrievedUsers = userRepository.findAll();
        assertNotNull(retrievedUsers);
        assertEquals(2, retrievedUsers.size());
        assertEquals("test@example.com", retrievedUsers.get(0).getEmail());
        assertEquals("John", retrievedUsers.get(0).getName());
    }
}
