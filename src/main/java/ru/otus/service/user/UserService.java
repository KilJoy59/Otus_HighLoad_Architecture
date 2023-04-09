package ru.otus.service.user;

import ru.otus.controller.dto.RegisterRequest;
import ru.otus.controller.dto.RegisterResponse;
import ru.otus.dto.UserDto;

import java.util.List;

public interface UserService {
    RegisterResponse register(RegisterRequest request);

    UserDto getIserByid(Long id);

    List<UserDto> findUsersByParams(String firstName, String secondName);

}
