package com.warlock.service;

import com.warlock.domain.StandCategory;
import com.warlock.repository.StandCategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StandCategoryServiceImpl implements StandCategoryService{

    @Autowired
    private final StandCategoryRepository standCategoryRepository;

    @Override
    public @NonNull List<StandCategory> findAll(){
        return standCategoryRepository.findAll();
    }

    @Override
    public @NonNull StandCategory findByName(@NonNull String standCategoryName){
        return standCategoryRepository.findByCategoryName(standCategoryName)
                .orElseThrow(() -> new EntityNotFoundException("Category " + standCategoryName + " is not found"));
    }
}
