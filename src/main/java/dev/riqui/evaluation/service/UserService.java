package dev.riqui.evaluation.service;

import dev.riqui.evaluation.dto.UserRequestDTO;
import dev.riqui.evaluation.dto.UserResponseDTO;
import dev.riqui.evaluation.exception.UserExistsException;
import dev.riqui.evaluation.exception.UserNotFoundException;
import dev.riqui.evaluation.mapper.UserMapper;
import dev.riqui.evaluation.model.Phone;
import dev.riqui.evaluation.model.User;
import dev.riqui.evaluation.repository.UserRepository;
import dev.riqui.evaluation.util.JwtUtil;
import dev.riqui.evaluation.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final UserMapper userMapper;

    public UserResponseDTO createUser(UserRequestDTO request) {
        ValidationUtil.validateSignUpRequest(request);
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserExistsException("El usuario ya existe");
        }
        var user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        Optional.ofNullable(request.getName())
                .ifPresent(user::setName);
        Optional.ofNullable(request.getPhones())
                .ifPresent(phoneDTOs -> {
                    Set<Phone> phones = phoneDTOs.stream()
                            .map(phoneDTO -> {
                                Phone phone = new Phone();
                                phone.setNumber(phoneDTO.getNumber());
                                phone.setCitycode(phoneDTO.getCityCode());
                                phone.setContrycode(phoneDTO.getCountryCode());
                                return phone;
                            })
                            .collect(Collectors.toSet());
                    user.setPhones(phones);
                });
        user.setCreated(LocalDateTime.now());
        user.setLastLogin(LocalDateTime.now());
        user.setActive(true);
        user.setToken(jwtUtil.generateToken(request.getEmail()));

        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }

    public UserResponseDTO loginUser(String token) {
        var decodedEmail = jwtUtil.getEmailFromToken(token);
        var user = userRepository.findByEmail(decodedEmail)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
        user.setLastLogin(LocalDateTime.now());
        user.setToken(jwtUtil.generateToken(user.getEmail()));

        var updatedUser = userRepository.save(user);
        return userMapper.toDTO(updatedUser);
    }

}
