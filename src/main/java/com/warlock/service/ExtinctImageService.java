package com.warlock.service;

import com.warlock.model.request.CreateExtinctImageRequest;
import com.warlock.model.response.ExtinctImageResponse;
import com.warlock.domain.ExtinctImage;
import lombok.NonNull;

import java.util.List;

public interface ExtinctImageService {
    @NonNull
    List<ExtinctImage> findAll();

    @NonNull
    ExtinctImage findById(@NonNull Long extinctImageId);

    @NonNull
    ExtinctImage createExtinctImage(@NonNull ExtinctImage request);

    @NonNull
    ExtinctImage update(@NonNull Long extinctImageId, @NonNull ExtinctImage request);

    void delete(@NonNull Long extinctImageId);
}
