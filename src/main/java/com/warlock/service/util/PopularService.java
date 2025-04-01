package com.warlock.service.util;

import com.warlock.domain.Extinct;
import com.warlock.domain.Stand;
import lombok.NonNull;

import java.util.List;

public interface PopularService {
    void cleanupOldStats();

    @NonNull
    List<Stand> getPopularStands(@NonNull Integer period);

    @NonNull
    List<Extinct> getPopularExtincts(@NonNull Integer period);
}
