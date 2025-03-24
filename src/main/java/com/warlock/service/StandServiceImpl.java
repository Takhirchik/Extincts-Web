package com.warlock.service;

import com.warlock.domain.User;
import com.warlock.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.warlock.domain.StandCategory;
import com.warlock.domain.Stand;
import com.warlock.model.request.CreateStandCategoryRequest;
import com.warlock.model.request.CreateStandRequest;
import com.warlock.model.response.StandCategoryResponse;
import com.warlock.model.response.StandResponse;
import com.warlock.repository.StandRepository;
import com.warlock.repository.StandCategoryRepository;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
public class StandServiceImpl implements StandService {
    private final UserRepository userRepository;
    private final StandRepository standRepository;
    private final StandCategoryRepository standCategoryRepository;

    //Получаем весь список пользователей
    @Override
    public @NonNull List<Stand> findAll() {
        return standRepository.findAll();
    }

    //Получаем пользователя по id
    @Override
    public @NonNull Stand findById(@NonNull Long standId) {
        return standRepository.findById(standId)
                .orElseThrow(() -> new EntityNotFoundException("Stand " + standId + " is not found"));
    }

    //Создаем пользователя
    @Override
    @Transactional
    public @NonNull Stand createStand(@NonNull Stand request) {
        return standRepository.save(request);
    }

    //Обновляем пользователя по id
    @Override
    @Transactional
    public @NonNull Stand update(@NonNull Long standId, @NonNull Stand request) {
        Stand stand = standRepository.findById(standId)
                .orElseThrow(() -> new EntityNotFoundException("Stand " + standId + " is not found"));
        standUpdate(stand, request);

        return standRepository.save(stand);
    }

    //Удаляем пользователя по id
    @Override
    public void delete(@NonNull Long standId) {
        standRepository.deleteById(standId);
    }

    private void standUpdate(@NonNull Stand stand, @NonNull Stand request) {
        ofNullable(request.getStandName()).map(stand::setStandName);
        ofNullable(request.getDescription()).map(stand::setDescription);
        ofNullable(request.getUser()).map(stand::setUser);
        ofNullable(request.getStandCategory()).map(stand::setStandCategory);
    }
}
