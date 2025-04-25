package com.warlock.unit.service;

import com.warlock.domain.Extinct;
import com.warlock.domain.ExtinctStats;
import com.warlock.domain.User;
import com.warlock.repository.ExtinctRepository;
import com.warlock.repository.ExtinctStatsRepository;
import com.warlock.service.ExtinctServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test-h2")
@ExtendWith(MockitoExtension.class)
class ExtinctServiceTest {

    @Mock
    private ExtinctRepository extinctRepository;
    
    @Mock
    private ExtinctStatsRepository statsRepository;
    
    @InjectMocks
    private ExtinctServiceImpl extinctService;

    private Extinct testExtinct;
    private User creator;

    @BeforeEach
    void setUp() {
        creator = new User();
        creator.setId(1L);
        testExtinct = new Extinct()
            .setExtinctName("Dodo")
            .setCreator(creator);
        testExtinct.setId(1L);
    }

    @Test
    void incrementLikes_ShouldUpdateStats() {
        when(extinctRepository.findById(1L)).thenReturn(Optional.of(testExtinct));
        when(statsRepository.findByExtinctAndDate(any(), any()))
            .thenReturn(Optional.of(new ExtinctStats().setLikes(5)));

        extinctService.incrementLikes(1L);

        verify(statsRepository).save(argThat(stat -> 
            stat.getLikes() == 6
        ));
    }

    @Test
    void createExtinct_ShouldSetDefaultValues() {
        when(extinctRepository.save(any())).thenReturn(testExtinct);

        Extinct result = extinctService.createExtinct(testExtinct);

        assertThat(result.getViews()).isZero();
        assertThat(result.getLikes()).isZero();
        assertThat(result.getCreatedAt()).isNotNull();
    }
}