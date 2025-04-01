package com.warlock.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.warlock.domain.Extinct;
import com.warlock.repository.ExtinctRepository;

import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDate;
import java.util.List;

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
public class ExtinctServiceImpl implements ExtinctService{

    @Autowired
    private final ExtinctRepository extinctRepository;

    //Получаем весь список пользователей
    @Override
    public @NonNull List<Extinct> findAll() {
        return extinctRepository.findAll();
    }

    //Получаем пользователя по id
    @Override
    public @NonNull Extinct findById(@NonNull Long extinctId) {
        return extinctRepository.findById(extinctId)
                .orElseThrow(() -> new EntityNotFoundException("Extinct " + extinctId + " is not found"));
    }

    //Создаем пользователя
    @Override
    @Transactional
    public @NonNull Extinct createExtinct(@NonNull Extinct request) {
        request.setCreatedAt(LocalDate.now());
        request.setViews(0);
        request.setLikes(0);
        return extinctRepository.save(request);
    }

    //Обновляем пользователя по id
    @Override
    @Transactional
    public @NonNull Extinct update(@NonNull Long extinctId, @NonNull Extinct request) {
        Extinct extinct = extinctRepository.findById(extinctId)
                .orElseThrow(() -> new EntityNotFoundException("Extinct " + extinctId + " is not found"));
        extinctUpdate(extinct, request);

        return extinctRepository.save(extinct);
    }

    //Удаляем пользователя по id
    @Override
    public void delete(@NonNull Long extinctId) {
        extinctRepository.deleteById(extinctId);
    }

    private void extinctUpdate(@NonNull Extinct extinct, @NonNull Extinct request) {
        ofNullable(request.getExtinctName()).map(extinct::setExtinctName);
        ofNullable(request.getDescription()).map(extinct::setDescription);
        ofNullable(request.getStand()).map(extinct::setStand);
        ofNullable(request.getUrlImage()).map(extinct::setUrlImage);
        ofNullable(request.getSmallThumbnailUrl()).map(extinct::setSmallThumbnailUrl);
        ofNullable(request.getMediumThumbnailUrl()).map(extinct::setMediumThumbnailUrl);
        ofNullable(request.getLargeThumbnailUrl()).map(extinct::setLargeThumbnailUrl);
    }

    @Override
    @Transactional
    public void incrementViews(@NonNull Long extinctId){
        Extinct extinct = extinctRepository.findById(extinctId)
                .orElseThrow(() -> new EntityNotFoundException("Extinct with id " + extinctId + " is not found"));
        extinct.setViews(extinct.getViews() + 1);
        extinctRepository.save(extinct);
    }

    @Override
    @Transactional
    public void incrementLikes(@NonNull Long extinctId){
        Extinct extinct = extinctRepository.findById(extinctId)
                .orElseThrow(() -> new EntityNotFoundException("Extinct with id " + extinctId + " is not found"));
        extinct.setLikes(extinct.getLikes() + 1);
        extinctRepository.save(extinct);
    }
}
