package ru.otus.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.controller.dto.RegisterRequest;
import ru.otus.controller.dto.RegisterResponse;
import ru.otus.dto.UserDto;
import ru.otus.exception.RegisterException;
import ru.otus.exception.UserFindException;
import ru.otus.exception.UserNotFoundException;
import ru.otus.repository.user.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public RegisterResponse register(RegisterRequest request) {
        Long userId = userRepository.addNewUser(request);
        if (userId == null) {
            throw new RegisterException("Ошибка при регистрации");
        }
        return new RegisterResponse(userId);
    }

    @Override
    public UserDto getIserByid(Long id) {
        UserDto user = userRepository.findUserByIdUser(id);
        if (user == null) {
            throw new UserNotFoundException("Пользователь не найден");
        }
        return user;
    }

    @Override
    public List<UserDto> findUsersByParams(String firstName, String secondName) {
        if (firstName == null || secondName == null) {
            throw new UserFindException("Params first_name and second_name must not be null or empty");
        }
        return userRepository.findUsersByParams(firstName, secondName);
    }
}
