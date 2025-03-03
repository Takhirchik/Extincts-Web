package com.warlock.service;

import com.warlock.model.request.CreateExtinctImageRequest;
import com.warlock.model.response.ExtinctImageResponse;
import lombok.NonNull;

import java.util.List;

public interface ExtinctImageService {
    @NonNull
    List<ExtinctImageResponse> findAll();

    @NonNull
    ExtinctImageResponse findById(@NonNull Long extinctImageId);

    @NonNull
    ExtinctImageResponse createExtinctImage(@NonNull CreateExtinctImageRequest request);

    @NonNull
    ExtinctImageResponse update(@NonNull Long extinctImageId, @NonNull CreateExtinctImageRequest request);

    void delete(@NonNull Long extinctImageId);
}
