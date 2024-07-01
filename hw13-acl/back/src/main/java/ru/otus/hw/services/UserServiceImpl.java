package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.otus.hw.dtos.UserDTO;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.repositories.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;


    @Override
    public List<UserDTO> findAll() {
        return userRepository.findAll()
            .stream()
            .map(UserDTO::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public UserDTO findByName(String username) {
        return UserDTO.toDto
            (
                userRepository.findUserByUsername(username)
                    .orElseThrow(() -> new EntityNotFoundException("User with name = %s not found".formatted(username)))
            );
    }

    @Override
    public void deleteUser(UserDTO dto) {

        userRepository.delete(UserDTO.fromDto(dto, encoder));
    }
}
