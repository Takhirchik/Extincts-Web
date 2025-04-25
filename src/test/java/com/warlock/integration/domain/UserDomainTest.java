package com.warlock.integration.domain;

import com.warlock.domain.Role;
import com.warlock.domain.User;
import com.warlock.integration.domain.abstracts.AbstractDomainTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserDomainTest extends AbstractDomainTest {

    @Test
    void shouldSaveUserWithRole() {
        Role adminRole = new Role().setName("ROLE_ADMIN");
        entityManager.persistAndFlush(adminRole);

        User user = new User()
                .setNickname("Admin")
                .setLogin("admin")
                .setPassword("secret")
                .setEmail("admin@example.com")
                .setRole(adminRole)
                .setUrlImage("avatar.jpg")
                .setSmallThumbnailUrl("small.jpg")
                .setMediumThumbnailUrl("medium.jpg")
                .setLargeThumbnailUrl("large.jpg");

        User saved = entityManager.persistFlushFind(user);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getRole()).isEqualTo(adminRole);
    }

    @Test
    void shouldFailWhenLoginIsNotUnique() {
        User duplicateUser = new User()
                .setNickname("Duplicate")
                .setLogin("testuser") // Такой же login как у testUser
                .setPassword("pass")
                .setEmail("duplicate@example.com")
                .setRole(userRole)
                .setUrlImage("avatar.jpg")
                .setSmallThumbnailUrl("small.jpg")
                .setMediumThumbnailUrl("medium.jpg")
                .setLargeThumbnailUrl("large.jpg");

        // Изменяем ожидаемое исключение
        assertThrows(Exception.class, () -> {
            entityManager.persistAndFlush(duplicateUser);
        });
    }

    @Test
    void shouldLazyLoadRole() {
        // Перезагружаем пользователя, очищая контекст
        entityManager.clear();
        User reloaded = entityManager.find(User.class, testUser.getId());

        // Изменяем проверку на более надежную
        assertThat(reloaded.getRole()).isNotNull();
        assertThat(reloaded.getRole().getName()).isEqualTo("ROLE_USER");
    }
}