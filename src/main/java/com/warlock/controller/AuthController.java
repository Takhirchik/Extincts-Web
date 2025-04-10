package com.warlock.controller;

import com.warlock.exceptions.PasswordConfirmationException;
import com.warlock.mapper.UserMapper;
import com.warlock.model.request.AuthenticateUserRequest;
import com.warlock.model.request.RegisterUserRequest;
import com.warlock.model.response.JwtTokenResponse;
import com.warlock.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Authentication API", description = "Регистрация и аутентификация User")
public class AuthController {

    @Autowired
    private final AuthenticationService authenticationService;

    @Autowired
    private final UserMapper userMapper;

    @Operation(
            summary = "Регистрация нового User",
            description = "Создает нового User и возвращает JWT токен",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешная регистрация",
                            content = @Content(schema = @Schema(implementation = JwtTokenResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Неверные данные запроса или пароли не совпадают",
                            content = @Content(
                                    examples = @ExampleObject(
                                            value = "{\"message\": \"Passwords are not identical\", \"timestamp\": \"2023-11-20T12:00:00Z\"}"
                                    )
                            )
                    )
            }
    )
    @PostMapping(value = "/sign-up")
    public ResponseEntity<JwtTokenResponse> signUp(@Valid @RequestBody RegisterUserRequest request){
        if (!request.getPassword().equals(request.getConfirmPassword())){
            throw new PasswordConfirmationException("Passwords are not identical");
        }
        var user = userMapper.fromRegisterRequestToEntity(request);

        var jwtToken = new JwtTokenResponse();
        return new ResponseEntity<>(jwtToken.setToken(authenticationService.register(user)), HttpStatus.OK);
    }

    @Operation(
            summary = "Аутентификация User",
            description = "Проверяет учетные данные и возвращает JWT токен",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешная аутентификация",
                            content = @Content(schema = @Schema(implementation = JwtTokenResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Неверные учетные данные",
                            content = @Content(
                                    examples = @ExampleObject(
                                            value = "{\"message\": \"Invalid credentials\", \"timestamp\": \"2023-11-20T12:00:00Z\"}"
                                    )
                            )
                    )
            }
    )
    @PostMapping(value = "/sign-in")
    public ResponseEntity<?> signIn(@Valid @RequestBody AuthenticateUserRequest request){
        var user = userMapper.fromAuthenticateRequestEntity(request);

        var jwtToken = new JwtTokenResponse();
        return new ResponseEntity<>(jwtToken.setToken(authenticationService.authenticate(user)), HttpStatus.OK);
    }
}
