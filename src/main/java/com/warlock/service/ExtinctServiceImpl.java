package com.warlock.service;

import com.warlock.domain.*;
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

    /**
     * Получить список всех экспонатов
     *
     * @return список экспонатов
     */
    @Cacheable(key = "'all'")
    @Override
    public @NonNull List<Extinct> findAll() {
        return extinctRepository.findAll();
    }

    /**
     * Найти экспонат по ID
     *
     * @param extinctId ID экспоната
     * @return Экспонат
     */
    @Cacheable(key = "#extinctId")
    @Override
    public @NonNull Extinct findById(@NonNull Long extinctId) {
        return extinctRepository.findById(extinctId)
                .orElseThrow(() -> new EntityNotFoundException("Extinct " + extinctId + " is not found"));
    }

    /**
     * Сохранить экспонат
     *
     * @param extinct экспонат
     * @return сохранённый экспонат
     */
    @CachePut(key = "#result.id")
    @Override
    @Transactional
    public @NonNull Extinct save(@NonNull Extinct extinct){
        return extinctRepository.save(extinct);
    }

    /**
     * Создать экспонат
     *
     * @param request запрос с экспонатом
     * @return созданный экспонат
     */
    @CacheEvict(key = "'all'")
    @Override
    public @NonNull Extinct createExtinct(@NonNull Extinct request) {
        request.setCreatedAt(LocalDate.now());
        request.setViews(0);
        request.setLikes(0);
        log.info("User {} creating extinct {}", request.getCreator(), request);
        return save(request);
    }

    /**
     * Обновить экспонат
     *
     * @param extinctId ID существующего экспоната
     * @param request запрос с новыми данными
     * @return обновлённый экспонат
     */
    @Override
    public @NonNull Extinct update(@NonNull Long extinctId, @NonNull Extinct request) {
        Extinct extinct = findById(extinctId);
        extinctUpdate(extinct, request);

        log.info("User {} updating extinct {} -> {}", extinct.getCreator(), extinct, request);
        return save(extinct);
    }

    /**
     * Удалить экспонат по ID
     *
     * @param extinctId ID экспоната
     */
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

    /**
     * Увеличить количество просмотров
     *
     * @param extinctId ID экспоната
     */
    @Override
    @Transactional
    public void incrementViews(@NonNull Long extinctId){
        Extinct extinct = findById(extinctId);
        extinct.setViews(extinct.getViews() + 1);
        save(extinct);
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

    /**
     * Увеличить количество лайков
     *
     * @param extinctId ID экспоната
     */
    @Override
    @Transactional
    public void incrementLikes(@NonNull Long extinctId){
        Extinct extinct = extinctRepository.findById(extinctId)
                .orElseThrow(() -> new EntityNotFoundException("Extinct with id " + extinctId + " is not found"));
        extinct.setLikes(extinct.getLikes() + 1);
        save(extinct);
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

    /**
     * проверка на создателя
     *
     * @param extinctId ID экспоната
     * @param user пользователь
     */
    @Override
    public void isCreator(@NonNull Long extinctId, @NonNull User user) {
        Extinct extinct = findById(extinctId);
        if (!extinct.getCreator().equals(user)){
            throw new AccessToResourcesException("Access denied [" + user + "]->[" + extinct + "]");
        }
    }

    /**
     * найти все экспонаты в стенде
     *
     * @param stand стенд
     * @return список экспонатов
     */
    @Cacheable(key = "'extincts:' + #stand.id")
    @Override
    public @NonNull List<Extinct> findAllExtincts(@NonNull Stand stand){
        return extinctRepository.findAllExtinctsByStandSortedByCreatedAt(stand);
    }


    /**
     * обновить изображения в экспонате
     *
     * @param extinctId ID экспоната
     * @param originalUrl URL изображения
     * @param smallThumbnailUrl маленькая миниатюра
     * @param mediumThumbnailUrl средняя миниатюра
     * @param largeThumbnailUrl большая миниатюра
     * @return обновленный экспонат
     */
    @Override
    public @NonNull Extinct updateExtinctImage(
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
        return save(extinct);
    }
}
