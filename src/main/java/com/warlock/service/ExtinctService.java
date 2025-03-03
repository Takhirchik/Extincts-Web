package com.warlock.service;

import com.warlock.model.request.CreateExtinctRequest;
import com.warlock.model.response.ExtinctResponse;
import lombok.NonNull;

import java.util.List;

public interface ExtinctService {
    @NonNull
    List<ExtinctResponse> findAll();

    @NonNull
    ExtinctResponse findById(@NonNull Long extinctId);

    @NonNull
    ExtinctResponse createStand(@NonNull CreateExtinctRequest request);

    @NonNull
    ExtinctResponse update(@NonNull Long extinctId, @NonNull CreateExtinctRequest request);

    void delete(@NonNull Long extinctId);
}
