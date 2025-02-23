package dev.riqui.evaluation.exception;

import dev.riqui.evaluation.dto.ErrorDetail;
import dev.riqui.evaluation.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            UserNotFoundException.class,
            InvalidEmailException.class,
            InvalidPasswordException.class,
            UserExistsException.class,
            JwtTokenException.class
    })
    public ResponseEntity<ErrorResponse> handleCustomExceptions(RuntimeException ex) {
        ErrorDetail errorDetail = new ErrorDetail(
                LocalDateTime.now(),
                determineErrorCode(ex),
                ex.getMessage()
        );
        ErrorResponse errorResponse = new ErrorResponse(List.of(errorDetail));

        return new ResponseEntity<>(errorResponse, determineHttpStatus(ex));
    }

    private int determineErrorCode(RuntimeException ex) {
        if (ex instanceof UserNotFoundException) return 404;
        if (ex instanceof UserExistsException) return 409;
        if (ex instanceof JwtTokenException) return 401;
        return 400; // Para InvalidEmailException e InvalidPasswordException
    }

    private HttpStatus determineHttpStatus(RuntimeException ex) {
        if (ex instanceof UserNotFoundException) return HttpStatus.NOT_FOUND;
        if (ex instanceof UserExistsException) return HttpStatus.CONFLICT;
        if (ex instanceof JwtTokenException) return HttpStatus.UNAUTHORIZED;
        return HttpStatus.BAD_REQUEST;
    }
}
