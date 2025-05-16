package com.warlock.integration.domain;

import com.warlock.domain.Stand;
import com.warlock.domain.StandStats;
import com.warlock.integration.domain.abstracts.AbstractDomainTest;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class StandStatsDomainTest extends AbstractDomainTest {

    @Test
    void shouldSaveStandStats() {
        Stand stand = createTestStand();
        entityManager.persistAndFlush(stand);

        StandStats stats = new StandStats()
                .setDate(LocalDate.now())
                .setViews(100)
                .setStand(stand);

        StandStats saved = entityManager.persistFlushFind(stats);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getStand()).isEqualTo(stand);
    }
}