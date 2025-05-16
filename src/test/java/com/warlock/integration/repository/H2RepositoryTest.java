package com.warlock.integration.repository;

import com.warlock.domain.*;
import com.warlock.integration.repository.abstracts.AbstractRepositoryTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test-h2")
public class H2RepositoryTest extends AbstractRepositoryTest {

    // RoleRepository
    @Test
    void roleRepository_findByName_shouldReturnRole() {
        roleRepository.save(new Role().setName("ROLE_ADMIN"));
        Optional<Role> found = roleRepository.findByName("ROLE_ADMIN");
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("ROLE_ADMIN");
    }

    // UserRepository
    @Test
    void userRepository_findByLogin_shouldReturnUser() {
        createTestUser("testuser", "test@example.com", "ROLE_USER");
        Optional<User> found = userRepository.findByLogin("testuser");
        assertThat(found).isPresent();
        assertThat(found.get().getLogin()).isEqualTo("testuser");
    }

    // StandRepository
    @Test
    void standRepository_findAll_shouldReturnSavedStands() {
        User creator = createTestUser("creator", "creator@test.com", "ROLE_USER");
        createTestStand(creator);
        List<Stand> stands = standRepository.findAll();
        assertThat(stands).hasSize(1);
    }

    // ExtinctRepository
    @Test
    void extinctRepository_findAllByStand_shouldReturnExtincts() {
        User creator = createTestUser("creator", "creator@test.com", "ROLE_USER");
        Stand stand = createTestStand(creator);
        createTestExtinct(stand, creator);
        
        List<Extinct> result = extinctRepository.findAllExtinctsByStandSortedByCreatedAt(stand);
        assertThat(result).hasSize(1);
    }

    // ExtinctStatsRepository
    @Test
    void extinctStatsRepository_findByExtinctAndDate_shouldReturnStats() {
        User creator = createTestUser("creator", "creator@test.com", "ROLE_USER");
        Stand stand = createTestStand(creator);
        Extinct extinct = createTestExtinct(stand, creator);
        LocalDate today = LocalDate.now();
        
        extinctStatsRepository.save(new ExtinctStats()
                .setExtinct(extinct)
                .setDate(today)
                .setViews(10)
                .setLikes(5));

        Optional<ExtinctStats> found = extinctStatsRepository.findByExtinctAndDate(extinct, today);
        assertThat(found).isPresent();
        assertThat(found.get().getViews()).isEqualTo(10);
    }
}