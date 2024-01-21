package core.service;

import core.model.dto.UserDto;
import core.model.dto.UserShortDto;
import core.model.entity.User;

import java.util.List;

public interface UserService {
    User addUser (UserDto userDto);

    List<UserShortDto> getAll(int pageNum, int pageSize);

    boolean saveUser(User user);
}
