package com.warlock.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.warlock.domain.Extinct;
import com.warlock.repository.ExtinctRepository;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
public class ExtinctServiceImpl implements ExtinctService{

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
        ofNullable(request.getUser()).map(extinct::setUser);
        ofNullable(request.getStand()).map(extinct::setStand);
    }
}
