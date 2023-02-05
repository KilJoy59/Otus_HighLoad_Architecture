package ru.otus.service.authorization;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.controller.dto.LoginRequest;
import ru.otus.controller.dto.LoginResponse;
import ru.otus.exception.UserNotFoundException;
import ru.otus.repository.user.UserRepository;
import ru.otus.util.JwtTokenUtil;
import ru.otus.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public LoginResponse login(LoginRequest request) {
        if (userExists(request)) {
            String token = jwtTokenUtil.generateToken(request.getUserId());
            return new LoginResponse(token);
        } else {
            throw new UserNotFoundException("Пользователь не найден");
        }
    }

    private boolean userExists(LoginRequest request) {
        String password = userRepository.findAndGetUserPaswordById(request.getUserId());
        if (password == null) {
            return false;
        }
        String passwordForVerification = StringUtils.MD5(request.getPassword());
        return passwordForVerification.equals(password);
    }

}
