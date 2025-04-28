package com.warlock.service;

import com.warlock.domain.ExtinctStats;
import com.warlock.domain.StandStats;
import com.warlock.domain.User;
import com.warlock.exceptions.AccessToResourcesException;
import com.warlock.repository.ExtinctStatsRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.warlock.domain.Extinct;
import com.warlock.repository.ExtinctRepository;

import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDate;
import java.util.List;

import static java.util.Optional.ofNullable;

@Slf4j
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "extincts")
public class ExtinctServiceImpl implements ExtinctService{

    @Autowired
    private final ExtinctRepository extinctRepository;

    @Autowired
    private final ExtinctStatsRepository extinctStatsRepository;

    //Получаем весь список пользователей
    @Cacheable(key = "'all'")
    @Override
    public @NonNull List<Extinct> findAll() {
        return extinctRepository.findAll();
    }

    //Получаем пользователя по id
    @Cacheable(key = "#extinctId")
    @Override
    public @NonNull Extinct findById(@NonNull Long extinctId) {
        return extinctRepository.findById(extinctId)
                .orElseThrow(() -> new EntityNotFoundException("Extinct " + extinctId + " is not found"));
    }

    //Создаем пользователя
    @CachePut(key = "#result.id")
    @Override
    @Transactional
    public @NonNull Extinct createExtinct(@NonNull Extinct request) {
        request.setCreatedAt(LocalDate.now());
        request.setViews(0);
        request.setLikes(0);
        log.info("User {} creating extinct {}", request.getCreator(), request);
        return extinctRepository.save(request);
    }

    //Обновляем пользователя по id
    @CachePut(key = "#extinctId")
    @Override
    @Transactional
    public @NonNull Extinct update(@NonNull Long extinctId, @NonNull Extinct request) {
        Extinct extinct = extinctRepository.findById(extinctId)
                .orElseThrow(() -> new EntityNotFoundException("Extinct " + extinctId + " is not found"));
        extinctUpdate(extinct, request);

        log.info("User {} updating extinct {} -> {}", extinct.getCreator(), extinct, request);
        return extinctRepository.save(extinct);
    }

    //Удаляем пользователя по id
    @CacheEvict(key = "#extinctId")
    @Override
    public void delete(@NonNull Long extinctId) {
        log.info("Deleting extinct {}", extinctId);
        extinctRepository.deleteById(extinctId);
    }

    private void extinctUpdate(@NonNull Extinct extinct, @NonNull Extinct request) {
        ofNullable(request.getExtinctName()).map(extinct::setExtinctName);
        ofNullable(request.getDescription()).map(extinct::setDescription);
        ofNullable(request.getStand()).map(extinct::setStand);
    }

    @CachePut(key = "#extinctId")
    @Override
    @Transactional
    public void incrementViews(@NonNull Long extinctId){
        Extinct extinct = extinctRepository.findById(extinctId)
                .orElseThrow(() -> new EntityNotFoundException("Extinct with id " + extinctId + " is not found"));
        extinct.setViews(extinct.getViews() + 1);
        extinctRepository.save(extinct);
        var date = LocalDate.now();
        ExtinctStats extinctStats = extinctStatsRepository.findByExtinctAndDate(extinct, date)
                .orElse(null);
        if (extinctStats == null){
            extinctStats = new ExtinctStats();
            extinctStats.setExtinct(extinct);
            extinctStats.setDate(date);
            extinctStats.setViews(1);
        } else {
            extinctStats.setViews(extinctStats.getViews() + 1);
        }
        extinctStatsRepository.save(extinctStats);
    }

    @CachePut(key = "#extinctId")
    @Override
    @Transactional
    public void incrementLikes(@NonNull Long extinctId){
        Extinct extinct = extinctRepository.findById(extinctId)
                .orElseThrow(() -> new EntityNotFoundException("Extinct with id " + extinctId + " is not found"));
        extinct.setLikes(extinct.getLikes() + 1);
        extinctRepository.save(extinct);
        var date = LocalDate.now();
        ExtinctStats extinctStats = extinctStatsRepository.findByExtinctAndDate(extinct, date)
                .orElse(null);
        if (extinctStats == null){
            extinctStats = new ExtinctStats();
            extinctStats.setExtinct(extinct);
            extinctStats.setDate(date);
            extinctStats.setViews(1);
            extinctStats.setLikes(1);
        } else {
            extinctStats.setLikes(extinctStats.getLikes() + 1);
        }
        extinctStatsRepository.save(extinctStats);
    }

    @Override
    public void isCreator(@NonNull Long extinctId, @NonNull User user) {
        Extinct extinct = extinctRepository.findById(extinctId)
                .orElseThrow(() -> new EntityNotFoundException("Extinct with id" + extinctId + " is not found"));
        if (!extinct.getCreator().equals(user)){
            throw new AccessToResourcesException("Access denied");
        }
    }

    @CachePut(key = "#extinctId")
    @Override
    public void updateExtinctImage(
            @NonNull Long extinctId,
            @NonNull String originalUrl,
            @NonNull String smallThumbnailUrl,
            @NonNull String mediumThumbnailUrl,
            @NonNull String largeThumbnailUrl
    ){
        log.info("Uploading image URL's to extinct with id {}", extinctId);
        var extinct = findById(extinctId)
                .setUrlImage(originalUrl)
                .setSmallThumbnailUrl(smallThumbnailUrl)
                .setMediumThumbnailUrl(mediumThumbnailUrl)
                .setLargeThumbnailUrl(largeThumbnailUrl);
        extinctRepository.save(extinct);
    }
}
