package dev.riqui.evaluation.util;

import dev.riqui.evaluation.dto.UserRequestDTO;
import dev.riqui.evaluation.exception.InvalidEmailException;
import dev.riqui.evaluation.exception.InvalidPasswordException;

import java.util.Optional;
import java.util.regex.Pattern;

public class ValidationUtil {
    private static final String EMAIL_REQUIRED = "El email es obligatorio";
    private static final String EMAIL_INVALID = "El formato del email no es válido";
    private static final String PASSWORD_REQUIRED = "La contraseña es obligatoria";
    private static final String PASSWORD_INVALID = "El formato de la contraseña no es válido";

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d.*\\d)[A-Za-z\\d]{8,12}$";

    public static void validateSignUpRequest(UserRequestDTO requestDTO) {
        Optional.ofNullable(requestDTO.getEmail())
                .filter(email -> !email.isBlank())
                .filter(ValidationUtil::isValidEmail)
                .orElseThrow(() -> new InvalidEmailException(
                        requestDTO.getEmail() == null || requestDTO.getEmail().isBlank()
                                ? EMAIL_REQUIRED : EMAIL_INVALID
                ));

        Optional.ofNullable(requestDTO.getPassword())
                .filter(pass -> !pass.isBlank())
                .filter(ValidationUtil::isValidPassword)
                .orElseThrow(() -> new InvalidPasswordException(
                        requestDTO.getPassword() == null || requestDTO.getPassword().isBlank()
                                ? PASSWORD_REQUIRED : PASSWORD_INVALID
                ));
    }

    private static boolean isValidEmail(String email) {
        return Pattern.compile(EMAIL_REGEX)
                .matcher(email)
                .matches();
    }

    private static boolean isValidPassword(String password) {
        return Pattern.compile(PASSWORD_REGEX)
                .matcher(password)
                .matches();
    }
}
