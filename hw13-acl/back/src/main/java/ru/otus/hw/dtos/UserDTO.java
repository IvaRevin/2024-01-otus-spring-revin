package ru.otus.hw.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.otus.hw.models.User;

@Data
@AllArgsConstructor
@SuperBuilder
@ToString
public class UserDTO {

    @NotNull
    private Long id;

    @NotBlank(message = "User username is can't be empty!")
    private String username;

    @NotBlank(message = "User password is can't be empty!")
    private String password;

    private Boolean enabled;

    public static User fromDto(UserDTO dto, PasswordEncoder encoder) {
        return User.builder()
            .id(dto.getId())
            .username(dto.getUsername())
            .password(encoder.encode(dto.getPassword()))
            .enabled(dto.getEnabled())
            .build();
    }

    public static UserDTO toDto(User user) {

        return UserDTO.builder()
            .id(user.getId())
            .username(user.getUsername())
            .password(user.getPassword())
            .enabled(user.getEnabled())
            .build();
    }
}
