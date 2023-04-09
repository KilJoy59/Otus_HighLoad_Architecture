package ru.otus.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.otus.dto.ErrorDto;

@Slf4j
@Component
@RestControllerAdvice
public class ExceptionHandlerComponent {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDto> handleUserNotfoundError(UserNotFoundException userNotFoundException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto(userNotFoundException.getErrorMessage()));
    }

    @ExceptionHandler(RegisterException.class)
    public ResponseEntity<ErrorDto> handleDataProfileNotFound(RegisterException registerException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto(registerException.getErrorMessage()));
    }

    @ExceptionHandler(UserFindException.class)
    public ResponseEntity<ErrorDto> handleUserFindError(UserFindException userFindException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(userFindException.getErrorMessage()));
    }



}
