package com.warlock.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.warlock.domain.Extinct;
import com.warlock.domain.ExtinctImage;
import com.warlock.model.request.CreateExtinctImageRequest;
import com.warlock.model.response.ExtinctImageResponse;
import com.warlock.repository.ExtinctImageRepository;
import com.warlock.repository.ExtinctRepository;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
public class ExtinctImageServiceImpl implements ExtinctImageService {

    private final ExtinctImageRepository extinctImageRepository;
    private final ExtinctRepository extinctRepository;

    @Override
    public @NonNull List<ExtinctImageResponse> findAll() {
        return extinctImageRepository.findAll()
                .stream()
                .map(this::buildExtinctImageResponse)
                .collect(Collectors.toList());
    }

    //Получаем пользователя по id
    @Override
    public @NonNull ExtinctImageResponse findById(@NonNull Long extinctImageId) {
        return extinctImageRepository.findById(extinctImageId)
                .map(this::buildExtinctImageResponse)
                .orElseThrow(() -> new EntityNotFoundException("Extinct Image " + extinctImageId + " is not found"));
    }

    //Создаем пользователя
    @Override
    @Transactional
    public @NonNull ExtinctImageResponse createExtinctImage(@NonNull CreateExtinctImageRequest request) {
        Long extinctId = request.getExtinct_id();
        Extinct extinct = extinctRepository.findById(extinctId)
                .orElseThrow(() -> new EntityNotFoundException("Extinct " + extinctId + " is not found"));

        ExtinctImage extinctImage = buildExtinctImageRequest(request, extinct);
        return buildExtinctImageResponse(extinctImageRepository.save(extinctImage));
    }

    //Обновляем пользователя по id
    @Override
    @Transactional
    public @NonNull ExtinctImageResponse update(@NonNull Long extinctImageId, @NonNull CreateExtinctImageRequest request) {
        ExtinctImage extinctImage = extinctImageRepository.findById(extinctImageId)
                .orElseThrow(() -> new EntityNotFoundException("Extinct Image" + extinctImageId + " is not found"));
        extinctUpdate(extinctImage, request);
        Long extinctId = request.getExtinct_id();
        if (extinctId != null){
            Extinct extinct = extinctRepository.findById(extinctId)
                    .orElseThrow(() -> new EntityNotFoundException(("Extinct " + extinctId + " is not found")));
            extinctImage.setExtinct(extinct);
        }
        return buildExtinctImageResponse(extinctImageRepository.save(extinctImage));
    }

    //Удаляем пользователя по id
    @Override
    public void delete(@NonNull Long extinctImageId) {
        extinctImageRepository.deleteById(extinctImageId);
    }


    private ExtinctImageResponse buildExtinctImageResponse(@NonNull ExtinctImage extinctImage) {
        return new ExtinctImageResponse()
                .setId(extinctImage.getId())
                .setUrlImage(extinctImage.getUrlImage())
                .setExtinct_id(extinctImage.getExtinct().getId());
    }

    private ExtinctImage buildExtinctImageRequest(@NonNull CreateExtinctImageRequest request, Extinct extinct) {
        return new ExtinctImage()
                .setUrlImage(request.getUrlImage())
                .setExtinct(extinct);
    }

    private void extinctUpdate(@NonNull ExtinctImage extinctImage, @NonNull CreateExtinctImageRequest request) {
        ofNullable(request.getUrlImage()).map(extinctImage::setUrlImage);
    }
}
