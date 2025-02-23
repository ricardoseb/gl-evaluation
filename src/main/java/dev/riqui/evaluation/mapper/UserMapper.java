package dev.riqui.evaluation.mapper;

import dev.riqui.evaluation.dto.PhoneDTO;
import dev.riqui.evaluation.dto.UserResponseDTO;
import dev.riqui.evaluation.model.Phone;
import dev.riqui.evaluation.model.User;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserResponseDTO toDTO(User user) {
        var dto = new UserResponseDTO();

        // Usando Optional con ifPresentOrElse (Java 11)
        Optional.ofNullable(user.getName())
                .ifPresentOrElse(
                        dto::setName,
                        () -> dto.setName("")
                );
        // Usando String.isBlank() (Java 11)
        var email = user.getEmail();
        if (!email.isBlank()) {
            dto.setEmail(email.strip());
        }
        dto.setId(user.getId());
        dto.setPassword(user.getPassword());
        dto.setCreated(user.getCreated());
        dto.setLastLogin(user.getLastLogin());
        dto.setToken(user.getToken());
        dto.setActive(user.isActive());

        dto.setPhones(Optional.ofNullable(user.getPhones())
                .map(phones -> phones.stream()
                        .map(this::toPhoneDTO)
                        .collect(Collectors.toSet()))
                .orElse(Set.of()));

        return dto;

    }
    private PhoneDTO toPhoneDTO(Phone phone) {
        var dto = new PhoneDTO();
        dto.setCityCode(phone.getCitycode());
        dto.setCountryCode(phone.getContrycode());
        dto.setNumber(phone.getNumber());
        return dto;
    }
}
