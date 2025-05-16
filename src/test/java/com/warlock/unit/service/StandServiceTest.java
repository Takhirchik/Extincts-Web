package com.warlock.unit.service;

import com.warlock.domain.Stand;
import com.warlock.domain.User;
import com.warlock.exceptions.AccessToResourcesException;
import com.warlock.repository.ExtinctRepository;
import com.warlock.repository.StandRepository;
import com.warlock.repository.StandStatsRepository;
import com.warlock.service.StandServiceImpl;
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
class StandServiceTest {

    @Mock
    private StandRepository standRepository;
    
    @Mock
    private ExtinctRepository extinctRepository;
    
    @Mock
    private StandStatsRepository statsRepository;
    
    @InjectMocks
    private StandServiceImpl standService;

    private Stand testStand;
    private User creator;

    @BeforeEach
    void setUp() {
        creator = new User()
                .setNickname("creator")
                .setLogin("creator_login")
                .setEmail("creator@test.com");
        creator.setId(1L);
        testStand = new Stand()
            .setStandName("Test Stand")
            .setCreator(creator);
        testStand.setId(1L);
    }

    @Test
    void incrementViews_ShouldCreateNewStatsIfNotExists() {
        when(standRepository.findById(1L)).thenReturn(Optional.of(testStand));
        when(statsRepository.findByStandAndDate(any(), any()))
            .thenReturn(Optional.empty());

        standService.incrementViews(1L);

        verify(statsRepository).save(argThat(stat -> 
            stat.getViews() == 1 && stat.getStand().equals(testStand)
        ));
    }

    @Test
    void isCreator_ShouldThrowForNonCreator() {
        when(standRepository.findById(1L)).thenReturn(Optional.of(testStand));
        User otherUser = new User()
                .setNickname("other")
                .setLogin("other_login")
                .setEmail("other@test.com");
        otherUser.setId(2L);

        assertThatThrownBy(() -> standService.isCreator(1L, otherUser))
            .isInstanceOf(AccessToResourcesException.class);
    }
}