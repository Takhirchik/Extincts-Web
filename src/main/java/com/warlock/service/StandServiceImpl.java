package com.warlock.service;

import com.warlock.domain.Extinct;
import com.warlock.domain.StandStats;
import com.warlock.domain.User;
import com.warlock.exceptions.AccessToResourcesException;
import com.warlock.repository.ExtinctRepository;
import com.warlock.repository.StandStatsRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.warlock.domain.Stand;
import com.warlock.repository.StandRepository;

import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDate;
import java.util.List;

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
public class StandServiceImpl implements StandService {

    @Autowired
    private final StandRepository standRepository;

    @Autowired
    private final ExtinctRepository extinctRepository;

    @Autowired
    private final StandStatsRepository standStatsRepository;

    /**
     * найти все стенды
     *
     * @return все существующие стенды
     */
    @Override
    public @NonNull List<Stand> findAll() {
        return standRepository.findAll();
    }

    /**
     * найти стенд по ID
     *
     * @param standId ID стенда
     * @return стэнд
     */
    @Override
    public @NonNull Stand findById(@NonNull Long standId) {
        return standRepository.findById(standId)
                .orElseThrow(() -> new EntityNotFoundException("Stand " + standId + " is not found"));
    }

    /**
     * создать стенд
     *
     * @param request стенд
     * @return созданный стенд
     */
    @Override
    @Transactional
    public @NonNull Stand createStand(@NonNull Stand request) {
        request.setCreatedAt(LocalDate.now());
        request.setViews(0);
        return standRepository.save(request);
    }

    /**
     * обновить стенд
     *
     * @param standId ID стенда который нужно обновить
     * @param request новые данные стенда
     * @return обновлённый стенд
     */
    @Override
    @Transactional
    public @NonNull Stand update(@NonNull Long standId, @NonNull Stand request) {
        Stand stand = standRepository.findById(standId)
                .orElseThrow(() -> new EntityNotFoundException("Stand " + standId + " is not found"));
        standUpdate(stand, request);

        return standRepository.save(stand);
    }

    /**
     * удалить стенд по ID
     *
     * @param standId ID стенда
     */
    @Override
    public void delete(@NonNull Long standId) {
        standRepository.deleteById(standId);
    }

    private void standUpdate(@NonNull Stand stand, @NonNull Stand request) {
        ofNullable(request.getStandName()).map(stand::setStandName);
        ofNullable(request.getDescription()).map(stand::setDescription);
    }

    /**
     * увеличить число просмотров
     *
     * @param standId ID стенда
     */
    @Override
    @Transactional
    public void incrementViews(@NonNull Long standId){
        Stand stand = standRepository.findById(standId)
                .orElseThrow(() -> new EntityNotFoundException("Stand with id " + standId + " is not found"));
        stand.setViews(stand.getViews() + 1);
        standRepository.save(stand);
        var date = LocalDate.now();
        StandStats standStats = standStatsRepository.findByStandAndDate(stand, date)
                .orElse(null);
        if (standStats == null){
            standStats = new StandStats();
            standStats.setStand(stand);
            standStats.setDate(date);
            standStats.setViews(1);
        } else {
            standStats.setViews(standStats.getViews() + 1);
        }
        standStatsRepository.save(standStats);

    }

    /**
     * найти все экспонаты в стенде
     *
     * @param stand стенд
     * @return список экспонатов
     */
    @Override
    public @NonNull List<Extinct> findAllExtincts(@NonNull Stand stand){
        return extinctRepository.findAllExtinctsByStandSortedByCreatedAt(stand);
    }

    /**
     * проверка на создателя
     *
     * @param standId ID стенда
     * @param user пользователь
     */
    @Override
    public void isCreator(@NonNull Long standId, @NonNull User user){
        Stand stand = standRepository.findById(standId)
                .orElseThrow(() -> new EntityNotFoundException("Stand with id " + standId + " is not found"));
        if (!stand.getCreator().equals(user)){
            throw new AccessToResourcesException("Access denied");
        }
    }

    /**
     * добавить экспонат
     *
     * @param standId ID стенда
     * @param extinctId ID экспоната
     */
    @Override
    public void addExtinct(@NonNull Long standId, @NonNull Long extinctId){
        Stand stand = standRepository.findById(standId)
                .orElseThrow(() -> new EntityNotFoundException("Stand with id " + standId + " is not found"));
        Extinct extinct = extinctRepository.findById(extinctId)
                .orElseThrow(() -> new EntityNotFoundException("Extinct " + extinctId + " is not found"));

        if (!stand.getCreator().equals(extinct.getCreator())){
            throw new RuntimeException("Cannot to add Extinct with another creator");
        }
        if (extinct.getStand() != null){
            throw new RuntimeException("Extinct already on stand");
        }
        stand.getExtincts().add(extinct);
        extinct.setStand(stand);
        standRepository.save(stand);
        extinctRepository.save(extinct);
    }

    /**
     * удалить экспонат
     *
     * @param standId ID стенда
     * @param extinctId ID экспоната
     */
    @Override
    public void deleteExtinct(@NonNull Long standId, @NonNull Long extinctId){
        Stand stand = standRepository.findById(standId)
                .orElseThrow(() -> new EntityNotFoundException("Stand with id " + standId + " is not found"));
        Extinct extinct = extinctRepository.findById(extinctId)
                .orElseThrow(() -> new EntityNotFoundException("Extinct " + extinctId + " is not found"));

        if (!stand.getExtincts().contains(extinct)){
            throw new RuntimeException("Extinct not on this stand");
        }
        stand.getExtincts().remove(extinct);
        extinct.setStand(null);
        standRepository.save(stand);
    }
}
