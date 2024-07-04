package ru.otus.hw.services;

import ru.otus.hw.dtos.UserDTO;

import java.util.List;

public interface UserService {

    List<UserDTO> findAll();

    UserDTO findByName(String name);

    void deleteUser(UserDTO dto);
}
