package dev.riqui.evaluation.controller;

import dev.riqui.evaluation.dto.ErrorResponse;
import dev.riqui.evaluation.dto.UserRequestDTO;
import dev.riqui.evaluation.dto.UserResponseDTO;
import dev.riqui.evaluation.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.validation.Valid;

@Controller("/api/user")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "API para gestión de usuarios")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Registrar un nuevo usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario creado exitosamente",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class),mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "El usuario ya existe",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))
    })
    @PostMapping(path = "/sign-up", produces = "application/json", consumes = "application/json")
    public ResponseEntity<UserResponseDTO> signUp(@RequestBody @Valid UserRequestDTO request) {
        var response = userService.createUser(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Iniciar sesión de usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login exitoso",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Token inválido o expirado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))
    })
    @GetMapping(path = "/login", produces = "application/json")
    public ResponseEntity<UserResponseDTO> login(@RequestHeader("Authorization") String token) {
        var response = userService.loginUser(token.replace("Bearer ", ""));
        return ResponseEntity.ok(response);
    }

}
