package com.warlock.service;

import com.warlock.domain.Extinct;
import com.warlock.domain.Stand;
import com.warlock.domain.User;
import lombok.NonNull;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

public interface ExtinctService {
    @NonNull
    List<Extinct> findAll();

    @NonNull
    Extinct findById(@NonNull Long extinctId);

    @NonNull
    Extinct save(@NonNull Extinct extinct);

    @NonNull
    Extinct createExtinct(@NonNull Extinct request);

    @NonNull
    Extinct update(@NonNull Long extinctId, @NonNull Extinct request);

    void delete(@NonNull Long extinctId);

    void incrementViews(@NonNull Long extinctId);

    void incrementLikes(@NonNull Long extinctId);

    void isCreator(@NonNull Long extinctId, @NonNull User user);

    @NonNull
    List<Extinct> findAllExtincts(@NonNull Stand stand);

    @NonNull
    Extinct updateExtinctImage(
            @NonNull Long extinctId,
            @NonNull String originalUrl,
            @NonNull String smallThumbnailUrl,
            @NonNull String mediumThumbnailUrl,
            @NonNull String largeThumbnailUrl
    );
}
