package core.model.dto;

import core.model.entity.Role;
import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private String surname;
    private String name;
    private String patronymic;
    private String email;
    private List<Role> roles;
}
