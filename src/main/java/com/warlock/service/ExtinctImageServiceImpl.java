package com.warlock.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.warlock.domain.ExtinctImage;
import com.warlock.repository.ExtinctImageRepository;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
public class ExtinctImageServiceImpl implements ExtinctImageService {

    @Autowired
    private final ExtinctImageRepository extinctImageRepository;

    @Override
    public @NonNull List<ExtinctImage> findAll() {
        return extinctImageRepository.findAll();
    }

    //Получаем пользователя по id
    @Override
    public @NonNull ExtinctImage findById(@NonNull Long extinctImageId) {
        return extinctImageRepository.findById(extinctImageId)
                .orElseThrow(() -> new EntityNotFoundException("Extinct Image " + extinctImageId + " is not found"));
    }

    //Создаем пользователя
    @Override
    @Transactional
    public @NonNull ExtinctImage createExtinctImage(@NonNull ExtinctImage request) {
        return extinctImageRepository.save(request);
    }

    //Обновляем пользователя по id
    @Override
    @Transactional
    public @NonNull ExtinctImage update(@NonNull Long extinctImageId, @NonNull ExtinctImage request) {
        ExtinctImage extinctImage = extinctImageRepository.findById(extinctImageId)
                .orElseThrow(() -> new EntityNotFoundException("Extinct Image" + extinctImageId + " is not found"));
        extinctUpdate(extinctImage, request);
        return extinctImageRepository.save(extinctImage);
    }

    //Удаляем пользователя по id
    @Override
    public void delete(@NonNull Long extinctImageId) {
        extinctImageRepository.deleteById(extinctImageId);
    }

    private void extinctUpdate(@NonNull ExtinctImage extinctImage, @NonNull ExtinctImage request) {
        ofNullable(request.getUrlImage()).map(extinctImage::setUrlImage);
        ofNullable(request.getExtinct()).map(extinctImage::setExtinct);
    }
}
