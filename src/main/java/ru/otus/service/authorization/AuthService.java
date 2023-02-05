package ru.otus.service.authorization;

import ru.otus.controller.dto.LoginRequest;
import ru.otus.controller.dto.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);

}
