package ru.otus.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RegisterException extends  RuntimeException {

    private final String errorMessage;

}
