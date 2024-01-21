package core.controller;

import core.exceptions.UserNotFoundException;
import core.model.dto.UserDto;
import core.model.dto.UserShortDto;
import core.model.entity.User;
import core.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.List;
@RestController
@RequiredArgsConstructor
public class UserController {
    private static final Logger LOGGER = LogManager.getLogger(UserController.class);

    private final UserService userService;

    @GetMapping(value = "/addUser")
    public UserDto showRegistrationForm() {
        return new UserDto();
    }

    @PostMapping(value = "/addUser")
    public ResponseEntity<String> addUser(@RequestBody UserDto userDto) {
        try {
            User user = userService.addUser(userDto);
            if (userService.saveUser(user)) {
                LOGGER.info("User added successfully");
                return ResponseEntity.ok("User added successfully");
            } else {
                LOGGER.error("Failed to add user");
                return ResponseEntity.badRequest().body("Failed to add user");
            }
        } catch (Exception e) {
            LOGGER.error("Error while adding user: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add user: " + e.getMessage());
        }
    }

    @GetMapping(value = "/users")
    public List<UserShortDto> getAll(@RequestParam(value = "pageNum",
            defaultValue = "1", required = false) int pageNum) {
        try {
            LOGGER.info("List of users provided");
//            http://localhost:8080/users?pageNum=1 - shows page No 1, ?pageNum=2 - page 2 etc.
            return userService.getAll(pageNum, 10);
        } catch (UserNotFoundException e) {
            LOGGER.error("Failed to show list of users");
            LOGGER.error("No users found: " + e.getMessage());
            return (List<UserShortDto>) ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        } catch (Exception e) {
            LOGGER.error("Failed to show list of users");
            LOGGER.error("Failed to retrieve list of users: " + e.getMessage());
            return (List<UserShortDto>) ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }
}
