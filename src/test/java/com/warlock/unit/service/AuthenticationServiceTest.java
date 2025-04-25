package com.warlock.unit.service;

import com.warlock.domain.Role;
import com.warlock.domain.User;
import com.warlock.service.AuthenticationServiceImpl;
import com.warlock.service.JwtService;
import com.warlock.service.RoleService;
import com.warlock.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.quality.Strictness;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT) // Разрешаем "лишние" моки
class AuthenticationServiceTest {
    @Mock
    private UserService userService;
    @Mock
    private RoleService roleService;
    @Mock
    private JwtService jwtService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    
    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    private User testUser;
    private Role userRole;

    @BeforeEach
    void setUp() {
        userRole = new Role().setName("ROLE_USER");
        testUser = new User()
                .setNickname("testuser")
                .setLogin("testuserLogin")
                .setEmail("test@example.com")
                .setPassword("password")
                .setRole(userRole);
        when(userService.userDetailsService())
                .thenReturn(username -> testUser);
        when(roleService.findByName("ROLE_USER")).thenReturn(userRole);
    }

    @Test
    void register_ShouldEncodePasswordAndReturnToken() {
        // Настройка моков
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userService.create(any(User.class))).thenReturn(testUser);
        when(jwtService.generateToken(any())).thenReturn("testToken");

        // Вызов метода
        String token = authenticationService.register(testUser);

        // Проверки
        assertThat(token).isEqualTo("testToken");
        verify(passwordEncoder).encode("password");
        verify(userService).create(argThat(user -> 
            user.getPassword().equals("encodedPassword") &&
            user.getRole().equals(userRole)
        ));
    }

    @Test
    void authenticate_ShouldReturnToken() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(testUser);
        when(authenticationManager.authenticate(any()))
                .thenReturn(authentication);
        when(jwtService.generateToken(testUser)).thenReturn("authToken");

        String token = authenticationService.authenticate(testUser);

        assertThat(token).isEqualTo("authToken");
    }

    @Test
    void authenticate_ShouldUseCorrectCredentials() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(testUser);
        when(authenticationManager.authenticate(any()))
                .thenReturn(authentication);

        User authRequest = new User()
                .setLogin("testuserLogin")
                .setPassword("password");

        authenticationService.authenticate(authRequest);

        verify(authenticationManager).authenticate(
                argThat(auth ->
                        auth.getPrincipal().equals("testuserLogin") &&
                                auth.getCredentials().equals("password")
                )
        );
    }

    @Test
    void authenticate_ShouldThrowOnInvalidCredentials() {
        // Настройка специфичных моков для этого теста
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        assertThatThrownBy(() -> authenticationService.authenticate(testUser))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessageContaining("Invalid credentials");
    }
}