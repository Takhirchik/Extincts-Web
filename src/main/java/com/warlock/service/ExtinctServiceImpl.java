package com.warlock.service;

import com.warlock.domain.Stand;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.warlock.domain.Extinct;
import com.warlock.model.request.CreateExtinctRequest;
import com.warlock.model.response.ExtinctResponse;
import com.warlock.repository.ExtinctRepository;
import com.warlock.repository.StandRepository;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
public class ExtinctServiceImpl implements ExtinctService{

    private final StandRepository standRepository;
    private final ExtinctRepository extinctRepository;

    //Получаем весь список пользователей
    @Override
    public @NonNull List<ExtinctResponse> findAll() {
        return extinctRepository.findAll()
                .stream()
                .map(this::buildExtinctResponse)
                .collect(Collectors.toList());
    }

    //Получаем пользователя по id
    @Override
    public @NonNull ExtinctResponse findById(@NonNull Long extinctId) {
        return extinctRepository.findById(extinctId)
                .map(this::buildExtinctResponse)
                .orElseThrow(() -> new EntityNotFoundException("Extinct " + extinctId + " is not found"));
    }

    //Создаем пользователя
    @Override
    @Transactional
    public @NonNull ExtinctResponse createExtinct(@NonNull CreateExtinctRequest request) {
        String standName = request.getStandName();
        Stand stand = standRepository.findByStandName(standName)
                .orElseThrow(() -> new EntityNotFoundException("Stand " + standName + " is not found"));
        Extinct extinct = buildExtinctRequest(request, stand);
        return buildExtinctResponse(extinctRepository.save(extinct));
    }

    //Обновляем пользователя по id
    @Override
    @Transactional
    public @NonNull ExtinctResponse update(@NonNull Long extinctId, @NonNull CreateExtinctRequest request) {
        Extinct extinct = extinctRepository.findById(extinctId)
                .orElseThrow(() -> new EntityNotFoundException("Extinct " + extinctId + " is not found"));
        extinctUpdate(extinct, request);
        String standName = request.getStandName();
        if (standName != null){
            Stand stand = standRepository.findByStandName(standName)
                    .orElseThrow(() -> new EntityNotFoundException(("Stand " + standName + " is not found")));
            extinct.setStand(stand);
        }
        return buildExtinctResponse(extinctRepository.save(extinct));
    }

    //Удаляем пользователя по id
    @Override
    public void delete(@NonNull Long extinctId) {
        extinctRepository.deleteById(extinctId);
    }

    private ExtinctResponse buildExtinctResponse(@NonNull Extinct extinct) {
        return new ExtinctResponse()
                .setId(extinct.getId())
                .setExtinctName(extinct.getExtinctName())
                .setDescription(extinct.getDescription())
                .setStandName(extinct.getStand().getStandName());
    }

    private Extinct buildExtinctRequest(@NonNull CreateExtinctRequest request, Stand stand) {
        return new Extinct()
                .setExtinctName(request.getExtinctName())
                .setDescription(request.getDescription())
                .setStand(stand);
    }

    private void extinctUpdate(@NonNull Extinct extinct, @NonNull CreateExtinctRequest request) {
        ofNullable(request.getExtinctName()).map(extinct::setExtinctName);
        ofNullable(request.getDescription()).map(extinct::setDescription);
    }
}
