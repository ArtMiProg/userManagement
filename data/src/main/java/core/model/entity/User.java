package core.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Surname should contain only Latin characters")
    @Size(max=40)
    @Column
    private String surname;
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Name should contain only Latin characters")
    @Size(max=20)
    @Column
    private String name;
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Patronymic should contain only Latin characters")
    @Size(max=40)
    @Column
    private String patronymic;
    @Size(max=50)
    @Email
    @Column
    private String email;
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Role> roles;
}
