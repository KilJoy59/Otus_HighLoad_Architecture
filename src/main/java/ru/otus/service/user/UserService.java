package ru.otus.service.user;

import ru.otus.controller.dto.RegisterRequest;
import ru.otus.controller.dto.RegisterResponse;
import ru.otus.dto.UserDto;

public interface UserService {
    RegisterResponse register(RegisterRequest request);

    UserDto getIserByid(Long id);

}
