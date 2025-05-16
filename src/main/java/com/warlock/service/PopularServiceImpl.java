package com.warlock.service;

import com.warlock.domain.Extinct;
import com.warlock.domain.Stand;
import com.warlock.repository.ExtinctRepository;
import com.warlock.repository.ExtinctStatsRepository;
import com.warlock.repository.StandRepository;
import com.warlock.repository.StandStatsRepository;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "popular")
public class PopularServiceImpl implements PopularService {

    @Autowired
    private final ExtinctStatsRepository extinctStatsRepository;

    @Autowired
    private final StandStatsRepository standStatsRepository;

    @Autowired
    private final StandRepository standRepository;

    @Autowired
    private final ExtinctRepository extinctRepository;

    private static final Integer MAX_PERIOD_DAYS = 30;

    @CacheEvict(allEntries = true)
    @Override
    @Scheduled(cron = "0 0 3 * * ?")
    @Transactional
    public void cleanupOldStats(){
        LocalDate threshold = LocalDate.now().minusDays(MAX_PERIOD_DAYS);
        standStatsRepository.deleteByDate(threshold);
        extinctStatsRepository.deleteByDate(threshold);
        log.info("Deleting old stats...");
    }

    @Cacheable(key = "'stands:' + #period")
    @Override
    public @NonNull List<Stand> getPopularStands(@NonNull Integer period){
        validateDate(period);
        LocalDate since = LocalDate.now().minusDays(period);

        List<Long> popularIds = standStatsRepository.findPopularStands(since);

        return standRepository.findAllById(popularIds);
    }

    @Cacheable(key = "'extincts:' + #period")
    @Override
    public @NonNull List<Extinct> getPopularExtincts(@NonNull Integer period){
        validateDate(period);
        LocalDate since = LocalDate.now().minusDays(period);

        List<Long> popularIds = extinctStatsRepository.findPopularExtincts(since);

        return extinctRepository.findAllById(popularIds);
    }

    private void validateDate(@NonNull Integer period){
        if (period < 1 || period > MAX_PERIOD_DAYS) {
            throw new IllegalArgumentException("Period should be between 1 and " + MAX_PERIOD_DAYS + " days");
        }
    }
}
