package com.warlock.integration.domain;

import com.warlock.domain.Extinct;
import com.warlock.domain.ExtinctStats;
import com.warlock.integration.domain.abstracts.AbstractDomainTest;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class ExtinctStatsDomainTest extends AbstractDomainTest {

    @Test
    void shouldSaveExtinctStats() {
        Extinct extinct = createTestExtinct();
        entityManager.persistAndFlush(extinct);

        ExtinctStats stats = new ExtinctStats()
                .setDate(LocalDate.now())
                .setViews(200)
                .setLikes(50)
                .setExtinct(extinct);

        ExtinctStats saved = entityManager.persistFlushFind(stats);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getExtinct()).isEqualTo(extinct);
    }
}