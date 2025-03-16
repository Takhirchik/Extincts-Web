package com.warlock.repository;

import com.warlock.domain.Role;
import com.warlock.domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRoleRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private User testUser;

    @BeforeEach
    public void setUp(){
        Role role = roleRepository.findByName("user")
                .orElseThrow(() -> new RuntimeException("Role user not found"));

        testUser = new User();
        testUser.setNickname("testNickname");
        testUser.setLogin("testLogin");
        testUser.setPassword("testPassword");
        testUser.setEmail("test@email.com");
        testUser.setRole(role);
        userRepository.save(testUser);
    }

    @AfterEach
    public void tearDown(){
        userRepository.delete(testUser);
    }

    @Test
    public void testFindUser() {
        User savedUser = userRepository.findById(testUser.getId()).orElse(null);

        assertNotNull(savedUser);
        assertEquals(testUser.getNickname(), savedUser.getNickname());
        assertEquals(testUser.getLogin(), savedUser.getLogin());
        assertEquals(testUser.getPassword(), savedUser.getPassword());
        assertEquals(testUser.getEmail(), savedUser.getEmail());
        assertEquals(testUser.getRole().getName(), savedUser.getRole().getName());
    }

    @Test
    public void testUpdateUser() {
        Role role = roleRepository.findByName("admin")
                .orElseThrow(() -> new RuntimeException("Role admin not found"));

        testUser.setRole(role);
        userRepository.save(testUser);

        User updatedUser = userRepository.findById(testUser.getId()).orElse(null);

        assertNotNull(updatedUser);
        assertEquals(testUser.getRole().getName(), updatedUser.getRole().getName());
    }
}
