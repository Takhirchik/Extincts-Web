package com.warlock.service;

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

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
public class StandServiceImpl implements StandService {
    private final StandRepository standRepository;
    private final StandCategoryRepository standCategoryRepository;

//    private static final String DEFAULT_ROLE = "user";

    //Получаем весь список пользователей
    @Override
    @Transactional(readOnly = true)
    public @NonNull List<StandResponse> findAll() {
        return standRepository.findAll()
                .stream()
                .map(this::buildStandResponse)
                .collect(Collectors.toList());
    }

    //Получаем пользователя по id
    @Override
    @Transactional(readOnly = true)
    public @NonNull StandResponse findById(@NonNull Long standId) {
        return standRepository.findById(standId)
                .map(this::buildStandResponse)
                .orElseThrow(() -> new EntityNotFoundException("Stand " + standId + " is not found"));
    }

    //Создаем пользователя
    @Override
    @Transactional
    public @NonNull StandResponse createStand(@NonNull CreateStandRequest request) {
        String categoryName = request.getStandCategory().getCategoryName();
        StandCategory standCategory = standCategoryRepository.findByName(categoryName)
                .orElseThrow(() -> new EntityNotFoundException("Category " + categoryName + " is not found"));
        Stand stand = buildStandRequest(request, standCategory);
        return buildStandResponse(standRepository.save(stand));
    }

    //Обновляем пользователя по id
    @Override
    @Transactional
    public @NonNull StandResponse update(@NonNull Long standId, @NonNull CreateStandRequest request) {
        Stand stand = standRepository.findById(standId)
                .orElseThrow(() -> new EntityNotFoundException("Stand " + standId + " is not found"));
        standUpdate(stand, request);
        if (stand.getStandCategory() != null){
            String categoryName = stand.getStandCategory().getCategoryName();
            StandCategory standCategory = standCategoryRepository.findByName(categoryName)
                    .orElseThrow(() -> new EntityNotFoundException(("Category " + categoryName + " is not found")));
            stand.setStandCategory(standCategory);
        }
        return buildStandResponse(standRepository.save(stand));
    }

    //Удаляем пользователя по id
    @Override
    @Transactional
    public void delete(@NonNull Long standId) {
        standRepository.deleteById(standId);
    }

    private StandResponse buildStandResponse(@NonNull Stand stand) {
        return new StandResponse()
                .setId(stand.getId())
                .setStandName(stand.getStandName())
                .setDescription(stand.getDescription())
                .setStandCategory(new StandCategoryResponse()
                        .setId(stand.getStandCategory().getId())
                        .setCategoryName(stand.getStandCategory().getCategoryName()));
    }

    private Stand buildStandRequest(@NonNull CreateStandRequest request, @NonNull StandCategory standCategory) {
        return new Stand()
                .setStandName(request.getStandName())
                .setDescription(request.getDescription())
                .setStandCategory(standCategory);
    }

    private void standUpdate(@NonNull Stand stand, @NonNull CreateStandRequest request) {
        ofNullable(request.getStandName()).map(stand::setStandName);
        ofNullable(request.getDescription()).map(stand::setDescription);

        CreateStandCategoryRequest standCategoryRequest = request.getStandCategory();
        if (standCategoryRequest != null) {
            ofNullable(standCategoryRequest.getCategoryName()).map(stand.getStandCategory()::setCategoryName);
        }
    }
}
