package com.warlock.unit.service;

import com.warlock.domain.Role;
import com.warlock.domain.User;
import com.warlock.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @Autowired
    private JwtService jwtService;
    private User testUser;
    private Role roleUser;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        // Устанавливаем значения через Reflection
        ReflectionTestUtils.setField(jwtService, "secretKey",
                "dc9086109fec08821a1e93528a25e6785d252db807a58b2d402d9cb5128efb84");
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 86400000L);

        roleUser = new Role().setName("ROLE_USER");
        testUser = new User()
                .setNickname("testNickname")
                .setLogin("testLogin")
                .setEmail("test@ex.com")
                .setPassword("testPassword")
                .setRole(roleUser);
    }

    @Test
    void generateToken_ShouldReturnValidToken() {
        String token = jwtService.generateToken(testUser);
        
        assertThat(token).isNotBlank();
        assertThat(jwtService.extractUsername(token)).isEqualTo("testLogin");
        assertTrue(jwtService.isTokenValid(token, testUser));
    }

    @Test
    void isTokenValid_ShouldReturnFalseForInvalidUser() {
        String token = jwtService.generateToken(testUser);
        User otherUser = new User()
                .setNickname("otheruser")
                .setLogin("otheruserLogin")
                .setPassword("otheruserLogin")
                .setEmail("otheruserEmail@ex.com")
                .setRole(roleUser);

        assertFalse(jwtService.isTokenValid(token, otherUser));
    }
}