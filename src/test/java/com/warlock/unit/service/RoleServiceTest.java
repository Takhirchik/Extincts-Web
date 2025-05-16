package com.warlock.unit.service;

import com.warlock.domain.Role;
import com.warlock.repository.RoleRepository;
import com.warlock.service.RoleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test-h2")
@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;
    
    @InjectMocks
    private RoleServiceImpl roleService;

    private Role adminRole;

    @BeforeEach
    void setUp() {
        adminRole = new Role().setName("ROLE_ADMIN");
    }

    @Test
    void findByName_ShouldReturnRole() {
        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(Optional.of(adminRole));
        
        Role result = roleService.findByName("ROLE_ADMIN");
        
        assertThat(result).isEqualTo(adminRole);
    }

    @Test
    void findByName_ShouldThrowWhenNotFound() {
        when(roleRepository.findByName("UNKNOWN")).thenReturn(Optional.empty());
        
        assertThatThrownBy(() -> roleService.findByName("UNKNOWN"))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessageContaining("not found");
    }
}