package com.warlock.service;

import com.warlock.domain.Extinct;
import com.warlock.domain.Stand;
import com.warlock.domain.User;
import lombok.NonNull;

import java.util.List;

public interface StandService {
    @NonNull
    List<Stand> findAll();

    @NonNull
    Stand findById(@NonNull Long standId);

    @NonNull
    Stand save(@NonNull Stand stand);

    @NonNull
    Stand createStand(@NonNull Stand request);

    @NonNull
    Stand update(@NonNull Long standId, @NonNull Stand request);

    void delete(@NonNull Long standId);

    void incrementViews(@NonNull Long standId);

    @NonNull
    List<Extinct> findAllExtincts(@NonNull Stand stand);

    void isCreator(@NonNull Long standId, @NonNull User user);

    void addExtinct(@NonNull Long standId, @NonNull Long extinctId);

    void deleteExtinct(@NonNull Long standId, @NonNull Long extinctId);
}
