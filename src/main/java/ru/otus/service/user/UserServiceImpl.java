package ru.otus.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.controller.dto.RegisterRequest;
import ru.otus.controller.dto.RegisterResponse;
import ru.otus.dto.UserDto;
import ru.otus.exception.RegisterException;
import ru.otus.exception.UserNotFoundException;
import ru.otus.repository.user.UserRepository;

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
}
