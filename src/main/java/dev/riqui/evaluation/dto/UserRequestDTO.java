package dev.riqui.evaluation.dto;

import lombok.Data;

import java.util.Set;

@Data
public class UserRequestDTO {

    private String name;
    private String email;
    private String password;
    private Set<PhoneDTO> phones;
}
