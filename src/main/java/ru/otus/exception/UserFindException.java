package ru.otus.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserFindException extends RuntimeException {

    private final String errorMessage;

}
