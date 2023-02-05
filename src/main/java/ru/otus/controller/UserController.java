package ru.otus.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.otus.controller.dto.RegisterRequest;
import ru.otus.controller.dto.RegisterResponse;
import ru.otus.dto.UserDto;
import ru.otus.service.user.UserService;
import ru.otus.util.dto.UserSecurityDto;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public RegisterResponse register(@RequestBody RegisterRequest request) {
        return userService.register(request);
    }

    @GetMapping("/get/{id}")
    public UserDto getUser(@AuthenticationPrincipal UserSecurityDto user, @PathVariable Long id) {
        return userService.getIserByid(id);
    }


}
