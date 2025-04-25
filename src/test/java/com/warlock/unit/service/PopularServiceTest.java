package com.warlock.unit.service;

import com.warlock.domain.Stand;
import com.warlock.repository.ExtinctRepository;
import com.warlock.repository.ExtinctStatsRepository;
import com.warlock.repository.StandRepository;
import com.warlock.repository.StandStatsRepository;
import com.warlock.service.PopularServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.test.context.ActiveProfiles;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test-h2")
@ExtendWith(MockitoExtension.class)
class PopularServiceTest {

    @Mock
    private ExtinctStatsRepository extinctStatsRepo;
    @Mock
    private StandStatsRepository standStatsRepo;
    @Mock
    private ExtinctRepository extinctRepo;
    @Mock
    private StandRepository standRepo;
    
    @InjectMocks
    private PopularServiceImpl popularService;

    @Test
    void cleanupOldStats_ShouldBeScheduledCorrectly() throws Exception {
        Method method = PopularServiceImpl.class.getDeclaredMethod("cleanupOldStats");
        Scheduled scheduled = method.getAnnotation(Scheduled.class);
        
        assertNotNull(scheduled);
        assertEquals("0 0 3 * * ?", scheduled.cron());
    }

    @Test
    void cleanupOldStats_ShouldCallWithCorrectDate() {
        // Мокаем LocalDate.now()
        LocalDate fixedDate = LocalDate.of(2023, 1, 15);
        try (var mockedLocalDate = mockStatic(LocalDate.class)) {
            mockedLocalDate.when(LocalDate::now).thenReturn(fixedDate);

            popularService.cleanupOldStats();
            
            verify(standStatsRepo).deleteByDate(fixedDate.minusDays(30));
            verify(extinctStatsRepo).deleteByDate(fixedDate.minusDays(30));
        }
    }

    @Test
    void getPopularExtincts_ShouldValidatePeriod() {
        assertThatThrownBy(() -> popularService.getPopularExtincts(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Period should be between");

        assertThatThrownBy(() -> popularService.getPopularExtincts(31))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    void getPopularStands_ShouldReturnSortedResults() {
        Stand stand1 = new Stand();
        stand1.setId(1L); // Устанавливаем ID отдельно

        Stand stand2 = new Stand();
        stand2.setId(2L);

        when(standStatsRepo.findPopularStands(any()))
                .thenReturn(List.of(1L, 2L));
        when(standRepo.findAllById(any()))
                .thenReturn(List.of(stand1, stand2));

        List<Stand> result = popularService.getPopularStands(7);

        assertThat(result)
                .hasSize(2)
                .extracting(Stand::getId)
                .containsExactly(1L, 2L);

        verify(standStatsRepo).findPopularStands(LocalDate.now().minusDays(7));
    }
}