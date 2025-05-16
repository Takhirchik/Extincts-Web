package com.warlock.unit.service;

import com.warlock.domain.Role;
import com.warlock.domain.User;
import com.warlock.repository.RoleRepository;
import com.warlock.repository.UserRepository;
import com.warlock.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test-h2")
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    
    @Mock
    private RoleRepository roleRepository;
    
    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private Role adminRole;

    @BeforeEach
    void setUp() {
        adminRole = new Role().setName("ROLE_ADMIN");
        testUser = new User()
            .setLogin("testuser")
            .setEmail("test@example.com")
            .setPassword("password");
        testUser.setId(1L);
    }

    @Test
    void findById_ShouldReturnUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        
        User result = userService.findById(1L);
        
        assertThat(result).isEqualTo(testUser);
    }

    @Test
    void findById_ShouldThrowWhenNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        
        assertThatThrownBy(() -> userService.findById(1L))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessageContaining("not found");
    }

    @Test
    void create_ShouldSaveNewUser() {
        when(userRepository.existsByLogin("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        
        User result = userService.create(testUser);
        
        assertThat(result).isEqualTo(testUser);
        verify(userRepository).save(testUser);
    }

    @Test
    void assignRole_ShouldSetAdminRole() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(Optional.of(adminRole));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.assignRole(1L);
        
        assertThat(result.getRole()).isEqualTo(adminRole);
        verify(userRepository).save(testUser);
    }

    @Test
    void getByLogin_ShouldThrowWhenUserNotFound() {
        when(userRepository.findByLogin("unknown")).thenReturn(Optional.empty());
        
        assertThatThrownBy(() -> userService.getByLogin("unknown"))
            .isInstanceOf(UsernameNotFoundException.class);
    }
}