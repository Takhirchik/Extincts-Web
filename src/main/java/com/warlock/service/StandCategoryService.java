package com.warlock.service;

import com.warlock.domain.StandCategory;
import lombok.NonNull;

import java.util.List;

public interface StandCategoryService {
    @NonNull
    List<StandCategory> findAll();

    @NonNull
    StandCategory findByName(@NonNull String standCategoryName);
}
