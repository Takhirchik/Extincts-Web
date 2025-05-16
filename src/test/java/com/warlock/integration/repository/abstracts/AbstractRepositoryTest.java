package com.warlock.integration.repository.abstracts;

import com.warlock.domain.Extinct;
import com.warlock.domain.Role;
import com.warlock.domain.Stand;
import com.warlock.domain.User;
import com.warlock.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class AbstractRepositoryTest {

    @Autowired protected TestEntityManager entityManager;
    
    @Autowired protected ExtinctRepository extinctRepository;
    @Autowired protected ExtinctStatsRepository extinctStatsRepository;
    @Autowired protected RoleRepository roleRepository;
    @Autowired protected StandRepository standRepository;
    @Autowired protected StandStatsRepository standStatsRepository;
    @Autowired protected UserRepository userRepository;

    protected User createTestUser(String login, String email, String roleName) {
        Role role = roleRepository.save(new Role().setName(roleName));
        return userRepository.save(new User()
                .setLogin(login)
                .setEmail(email)
                .setNickname("Test User")
                .setPassword("password")
                .setRole(role));
    }

    protected Stand createTestStand(User creator) {
        return standRepository.save(new Stand()
                .setStandName("Test Stand")
                .setDescription("Test Description")
                .setCreator(creator));
    }

    protected Extinct createTestExtinct(Stand stand, User creator) {
        return extinctRepository.save(new Extinct()
                .setExtinctName("Test Extinct")
                .setDescription("Test Description")
                .setCreator(creator)
                .setStand(stand)
                .setUrlImage("test.jpg")
                .setSmallThumbnailUrl("small.jpg")
                .setMediumThumbnailUrl("medium.jpg")
                .setLargeThumbnailUrl("large.jpg"));
    }
}