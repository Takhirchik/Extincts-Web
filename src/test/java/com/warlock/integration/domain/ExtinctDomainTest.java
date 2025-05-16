package com.warlock.integration.domain;

import com.warlock.domain.Extinct;
import com.warlock.domain.ExtinctStats;
import com.warlock.integration.domain.abstracts.AbstractDomainTest;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class ExtinctDomainTest extends AbstractDomainTest {

    @Test
    void shouldSaveExtinctWithAllFields() {
        Extinct extinct = createTestExtinct();
        Extinct saved = entityManager.persistFlushFind(extinct);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getExtinctName()).isEqualTo("Test Extinct");
    }

    @Test
    void shouldCascadeStats() {
        Extinct extinct = createTestExtinct();
        entityManager.persistAndFlush(extinct);

        // Вместо создания новой коллекции, модифицируем существующую
        ExtinctStats stats = new ExtinctStats()
                .setDate(LocalDate.now())
                .setViews(100)
                .setLikes(20)
                .setExtinct(extinct);

        // Получаем существующую коллекцию и добавляем элемент
        extinct.getExtinctStats().add(stats);

        Extinct saved = entityManager.persistFlushFind(extinct);

        assertThat(saved.getExtinctStats()).hasSize(1);
    }
}