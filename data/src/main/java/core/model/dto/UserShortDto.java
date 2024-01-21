package core.model.dto;

import core.model.entity.Role;
import lombok.Data;

import java.util.List;

@Data
public class UserShortDto {
    private String fullName;
    private String email;
    private List<Role> roles;
}
