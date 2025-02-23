package dev.riqui.evaluation.service;

import dev.riqui.evaluation.dto.PhoneDTO;
import dev.riqui.evaluation.dto.UserRequestDTO;
import dev.riqui.evaluation.dto.UserResponseDTO;
import dev.riqui.evaluation.exception.InvalidEmailException;
import dev.riqui.evaluation.exception.InvalidPasswordException;
import dev.riqui.evaluation.exception.UserExistsException;
import dev.riqui.evaluation.mapper.UserMapper;
import dev.riqui.evaluation.model.User;
import dev.riqui.evaluation.repository.UserRepository;
import dev.riqui.evaluation.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserService userService;

    @Test
    void createUser_Success() {
        // Given
        UserRequestDTO request = createValidRequest();
        User user = new User();
        UserResponseDTO expectedResponse = new UserResponseDTO();
        String token = "token";

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(jwtUtil.generateToken(request.getEmail())).thenReturn(token);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDTO(user)).thenReturn(expectedResponse);

        // When
        UserResponseDTO response = userService.createUser(request);

        // Then
        assertNotNull(response);
        verify(userRepository).findByEmail(request.getEmail());
        verify(userRepository).save(any(User.class));
        verify(userMapper).toDTO(user);
    }

    @Test
    void createUser_InvalidEmail() {
        // Given
        UserRequestDTO request = new UserRequestDTO();
        request.setEmail("invalid-email");

        // When/Then
        assertThrows(InvalidEmailException.class, () -> userService.createUser(request));
    }

    @Test
    void createUser_UserExists() {
        // Given
        UserRequestDTO request = createValidRequest();
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(new User()));

        // When/Then
        assertThrows(UserExistsException.class, () -> userService.createUser(request));
    }

    @Test
    void loginUser_Success() {
        // Given
        String token = "valid-token";
        String email = "test@test.com";
        User user = new User();
        user.setEmail(email);
        UserResponseDTO expectedResponse = new UserResponseDTO();

        when(jwtUtil.getEmailFromToken(token)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken(any(String.class))).thenReturn("new-token");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDTO(user)).thenReturn(expectedResponse);

        // When
        UserResponseDTO response = userService.loginUser(token);

        // Then
        assertNotNull(response);
        verify(userRepository).findByEmail(email);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_NullEmail() {
        // Given
        UserRequestDTO request = new UserRequestDTO();
        request.setPassword("Test12abc");

        // When/Then
        InvalidEmailException exception = assertThrows(InvalidEmailException.class,
                () -> userService.createUser(request));
        assertEquals("El email es obligatorio", exception.getMessage());
    }

    @Test
    void createUser_BlankEmail() {
        // Given
        UserRequestDTO request = new UserRequestDTO();
        request.setEmail("   ");
        request.setPassword("Test12abc");

        // When/Then
        InvalidEmailException exception = assertThrows(InvalidEmailException.class,
                () -> userService.createUser(request));
        assertEquals("El email es obligatorio", exception.getMessage());
    }

    @Test
    void createUser_InvalidEmailFormat() {
        // Given
        UserRequestDTO request = new UserRequestDTO();
        request.setEmail("invalid-email");
        request.setPassword("Test12abc");

        // When/Then
        InvalidEmailException exception = assertThrows(InvalidEmailException.class,
                () -> userService.createUser(request));
        assertEquals("El formato del email no es válido", exception.getMessage());
    }

    @Test
    void createUser_NullPassword() {
        // Given
        UserRequestDTO request = new UserRequestDTO();
        request.setEmail("test@test.com");

        // When/Then
        InvalidPasswordException exception = assertThrows(InvalidPasswordException.class,
                () -> userService.createUser(request));
        assertEquals("La contraseña es obligatoria", exception.getMessage());
    }

    @Test
    void createUser_BlankPassword() {
        // Given
        UserRequestDTO request = new UserRequestDTO();
        request.setEmail("test@test.com");
        request.setPassword("  ");

        // When/Then
        InvalidPasswordException exception = assertThrows(InvalidPasswordException.class,
                () -> userService.createUser(request));
        assertEquals("La contraseña es obligatoria", exception.getMessage());
    }

    @Test
    void createUser_InvalidPasswordFormat() {
        // Given
        UserRequestDTO request = new UserRequestDTO();
        request.setEmail("test@test.com");
        request.setPassword("invalid"); // Sin mayúscula ni números

        // When/Then
        InvalidPasswordException exception = assertThrows(InvalidPasswordException.class,
                () -> userService.createUser(request));
        assertEquals("El formato de la contraseña no es válido", exception.getMessage());
    }

    @Test
    void createUser_WithoutOptionalFields_Success() {
        // Given
        UserRequestDTO request = new UserRequestDTO();
        request.setEmail("test@test.com");
        request.setPassword("Test12abc");
        // No setting name and phones (optional fields)

        User user = new User();
        UserResponseDTO expectedResponse = new UserResponseDTO();
        String token = "token";

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(jwtUtil.generateToken(request.getEmail())).thenReturn(token);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDTO(user)).thenReturn(expectedResponse);

        // When
        UserResponseDTO response = userService.createUser(request);

        // Then
        assertNotNull(response);
        verify(userRepository).findByEmail(request.getEmail());
        verify(userRepository).save(any(User.class));
        verify(userMapper).toDTO(user);
    }


    private UserRequestDTO createValidRequest() {
        UserRequestDTO request = new UserRequestDTO();
        request.setEmail("test@test.com");
        request.setPassword("Test12abc");
        request.setName("Test User");

        PhoneDTO phone = new PhoneDTO();
        phone.setNumber(1234567890L);
        phone.setCityCode(1);
        phone.setCountryCode("57");

        request.setPhones(Set.of(phone));

        return request;
    }
}