package com.warlock.service;

import com.warlock.model.response.StandResponse;
import com.warlock.model.request.CreateStandRequest;
import lombok.NonNull;

import java.util.List;

public interface StandService {
    @NonNull
    List<StandResponse> findAll();

    @NonNull
    StandResponse findById(@NonNull Long standId);

    @NonNull
    StandResponse createStand(@NonNull CreateStandRequest request);

    @NonNull
    StandResponse update(@NonNull Long standId, @NonNull CreateStandRequest request);

    void delete(@NonNull Long standId);
}
