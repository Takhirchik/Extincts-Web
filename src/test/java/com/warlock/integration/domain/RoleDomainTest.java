package com.warlock.integration.domain;

import com.warlock.domain.Role;
import com.warlock.integration.domain.abstracts.AbstractDomainTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RoleDomainTest extends AbstractDomainTest {

    @Test
    void shouldSaveRole() {
        Role role = new Role().setName("ROLE_MODERATOR");
        Role saved = entityManager.persistFlushFind(role);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("ROLE_MODERATOR");
    }

    @Test
    void shouldFailWhenNameIsNotUnique() {
        Role duplicateRole = new Role().setName("ROLE_USER"); // Такое же имя как у userRole

        assertThrows(Exception.class, () -> {
            entityManager.persistAndFlush(duplicateRole);
        });
    }
}