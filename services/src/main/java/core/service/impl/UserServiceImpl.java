package core.service.impl;

import core.exceptions.InvalidUserInputException;
import core.exceptions.UserNotFoundException;
import core.model.dto.UserDto;
import core.model.dto.UserShortDto;
import core.model.entity.Role;
import core.model.entity.User;
import core.model.repository.UserRepository;
import core.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LogManager.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    private User convertToUser(UserDto userDto) {
        User user = new User();
        user.setSurname(userDto.getSurname());
        user.setName(userDto.getName());
        user.setPatronymic(userDto.getPatronymic());
        user.setEmail(userDto.getEmail());
        user.setRoles(Arrays.asList(Role.ROLE_SALE_USER));
        return user;
    }

    @Override
    public User addUser(UserDto userDto) {
        try {
            if (userDto.getEmail() == null || userDto.getEmail().isEmpty()) {
                throw new InvalidUserInputException("Email cannot be empty");
            }
            User user = convertToUser(userDto);
            LOGGER.info("Service method addUser executed");
            return user;
        } catch (InvalidUserInputException e) {
            LOGGER.error("Service method addUser failed");
            LOGGER.error("Invalid user input: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            LOGGER.error("Service method addUser failed");
            LOGGER.error("Error while adding user: " + e.getMessage());
            throw new RuntimeException("Failed to add user", e);
        }
    }

    private UserShortDto convertToDto(User user) {
        UserShortDto userShortDto = new UserShortDto();
        String fullName = user.getSurname() + " " + user.getName() + " " + user.getPatronymic();
        userShortDto.setFullName(fullName);
        userShortDto.setEmail(user.getEmail());
        userShortDto.setRoles(Arrays.asList(Role.ROLE_SALE_USER));
        return userShortDto;
    }
    @Override
    public List<UserShortDto> getAll(int pageNum, int pageSize) {
        try {
            List<User> users = userRepository.findAll();
            if (pageNum < 1){
                pageNum = 1;
            }
            pageNum = pageNum - 1;
            if(pageNum > (users.size()/10 - 1)){
                pageNum = users.size()/10;
            }
//            http://localhost:8080/users?pageNum=1 - shows page No 1, ?pageNum=2 - page 2 etc.

            Sort sort = Sort.by("email").ascending();
            Pageable pageable = PageRequest.of(pageNum, 10, sort);
            Page<User> usersPage = userRepository.findAll(pageable);
            List<UserShortDto> usersShortDto = new ArrayList<>();
            for (User user : usersPage.getContent()) {
                UserShortDto userShortDto = convertToDto(user);
                usersShortDto.add(userShortDto);
            }

            LOGGER.info("Service method getAll executed");
            if (users.isEmpty()) {
                throw new UserNotFoundException("No users found");
            }
            return usersShortDto;
        } catch (Exception e) {
            LOGGER.error("Service method getAll failed");
            LOGGER.error("Error while retrieving users: " + e.getMessage());
            throw new RuntimeException("Failed to retrieve users", e);
        }
    }

    @Override
    public boolean saveUser(User user) {
        try {
            userRepository.save(user);
            LOGGER.info("Service method saveUser executed");
            return true;
        } catch (Exception e) {
            LOGGER.error("Service method saveUser failed");
            throw new RuntimeException("Failed to save user", e);
        }
    }
}
