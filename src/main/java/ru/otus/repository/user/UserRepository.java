package ru.otus.repository.user;

import ru.otus.controller.dto.RegisterRequest;
import ru.otus.dto.UserDto;
import ru.otus.util.dto.UserSecurityDto;

public interface UserRepository {
    Long addNewUser(RegisterRequest request);

    String findAndGetUserPaswordById(Long userId);

    UserDto findUserByIdUser(Long id);

    UserSecurityDto checkExistsUserAndGetUser(Long idUser);

}
