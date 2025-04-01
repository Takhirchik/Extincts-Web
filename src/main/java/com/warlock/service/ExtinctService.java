package com.warlock.service;

import com.warlock.domain.Extinct;
import lombok.NonNull;

import java.util.List;

public interface ExtinctService {
    @NonNull
    List<Extinct> findAll();

    @NonNull
    Extinct findById(@NonNull Long extinctId);

    @NonNull
    Extinct createExtinct(@NonNull Extinct request);

    @NonNull
    Extinct update(@NonNull Long extinctId, @NonNull Extinct request);

    void delete(@NonNull Long extinctId);

    void incrementViews(@NonNull Long extinctId);

    void incrementLikes(@NonNull Long extinctId);
}
