package com.warlock.integration.repository;

import com.warlock.domain.Extinct;
import com.warlock.domain.ExtinctStats;
import com.warlock.domain.Stand;
import com.warlock.domain.User;
import com.warlock.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test-postgres")
@Transactional
public class PostgresRepositoryTest {

    @Autowired private ExtinctRepository extinctRepository;
    @Autowired private ExtinctStatsRepository extinctStatsRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private StandRepository standRepository;
    @Autowired private StandStatsRepository standStatsRepository;
    @Autowired private UserRepository userRepository;
    
    // UserRepository
    @Test
    void userRepository_searchUsers_shouldFindByFullText() {
        List<User> result = userRepository.searchUsers("администратор");
        assertThat(result)
            .hasSize(1)
            .extracting(User::getLogin)
            .containsExactly("admin");
    }

    // StandRepository
    @Test
    void standRepository_searchStands_shouldFindByFullText() {
        List<Stand> result = standRepository.searchStands("динозавр", "relevance");
        assertThat(result)
            .hasSize(1)
            .extracting(Stand::getStandName)
            .containsExactly("Эра Динозавров");
    }

    // ExtinctRepository
    @Test
    void extinctRepository_searchExtincts_shouldFindByFullText() {
        List<Extinct> result = extinctRepository.searchExtincts("хищник", "relevance");
        assertThat(result)
            .hasSize(2)
            .extracting(Extinct::getExtinctName)
            .containsExactly("Тираннозавр Рекс", "Саблезубый Тигр");
    }

    // ExtinctStatsRepository tests
    @Test
    void extinctStatsRepository_findPopularExtincts_shouldReturnOrderedIds() {
        Extinct extinct = extinctRepository.findById(1L).get();
        LocalDate today = LocalDate.now();
        
        extinctStatsRepository.save(new ExtinctStats()
                .setExtinct(extinct)
                .setDate(today)
                .setViews(10)
                .setLikes(5));

        List<Long> popularIds = extinctStatsRepository.findPopularExtincts(today.minusDays(1));
        assertThat(popularIds).containsExactly(extinct.getId());
    }
}