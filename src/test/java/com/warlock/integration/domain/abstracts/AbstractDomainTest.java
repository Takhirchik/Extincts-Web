package com.warlock.integration.domain.abstracts;

import com.warlock.domain.Extinct;
import com.warlock.domain.Role;
import com.warlock.domain.Stand;
import com.warlock.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test-h2")
@DataJpaTest
public class AbstractDomainTest {

    @Autowired
    protected TestEntityManager entityManager;

    protected User testUser;
    protected Role userRole;

    @BeforeEach
    void setUp() {
        userRole = new Role().setName("ROLE_USER");
        entityManager.persistAndFlush(userRole);

        testUser = new User()
                .setNickname("Test User")
                .setLogin("testuser")
                .setPassword("password")
                .setEmail("test@example.com")
                .setRole(userRole)
                .setUrlImage("avatar.jpg")
                .setSmallThumbnailUrl("small.jpg")
                .setMediumThumbnailUrl("medium.jpg")
                .setLargeThumbnailUrl("large.jpg");
        entityManager.persistAndFlush(testUser);
    }

    protected Extinct createTestExtinct() {
        return new Extinct()
                .setExtinctName("Test Extinct")
                .setDescription("Description")
                .setUrlImage("image.jpg")
                .setSmallThumbnailUrl("small.jpg")
                .setMediumThumbnailUrl("medium.jpg")
                .setLargeThumbnailUrl("large.jpg")
                .setCreator(testUser);
    }

    protected Stand createTestStand() {
        return new Stand()
                .setStandName("Test Stand")
                .setDescription("Description")
                .setCreator(testUser);
    }
}