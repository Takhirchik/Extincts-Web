package com.warlock.integration.service;

import com.warlock.domain.Role;
import com.warlock.domain.User;
import com.warlock.repository.RoleRepository;
import com.warlock.repository.UserRepository;
import com.warlock.service.RoleService;
import com.warlock.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test-h2")
@Transactional
class UserServiceTest {

    @Autowired
    private TestEntityManager entityManager;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleService roleService;

    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, roleService);
    }

    @Test
    @Transactional
    void update_ShouldModifyUserData() {
        Role role = roleService.findByName("ROLE_USER");
        User existing = new User();
        existing.setNickname("extistingNickname");
        existing.setLogin("existing");
        existing.setEmail("existing@test.com");
        existing.setPassword("password");
        existing.setRole(role);
        existing = userRepository.save(existing);

        User updates = new User();
        updates.setNickname("New Nickname");

        User updated = userService.update(existing.getId(), updates);

        assertThat(updated.getNickname()).isEqualTo("New Nickname");
        assertThat(userRepository.findById(existing.getId()).get().getNickname())
                .isEqualTo("New Nickname");
    }
}