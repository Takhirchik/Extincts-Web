package com.warlock.service;

import com.warlock.domain.Stand;
import lombok.NonNull;

import java.util.List;

public interface StandService {
    @NonNull
    List<Stand> findAll();

    @NonNull
    Stand findById(@NonNull Long standId);

    @NonNull
    Stand createStand(@NonNull Stand request);

    @NonNull
    Stand update(@NonNull Long standId, @NonNull Stand request);

    void delete(@NonNull Long standId);
}
