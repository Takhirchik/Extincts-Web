package com.warlock.factory;

import com.warlock.domain.StandCategory;
import com.warlock.model.request.CreateStandCategoryRequest;
import com.warlock.model.response.StandCategoryResponse;
import org.springframework.stereotype.Component;

@Component
public class StandCategoryFactoryDTO {

    public StandCategory fromDTOToEntity(CreateStandCategoryRequest request){
        StandCategory standCategory = new StandCategory();
        standCategory.setCategoryName(request.getCategoryName());
        return standCategory;
    }

    public StandCategoryResponse fromEntityToDTO(StandCategory standCategory){
        StandCategoryResponse standCategoryResponse = new StandCategoryResponse();
        standCategoryResponse.setId(standCategory.getId());
        standCategoryResponse.setCategoryName(standCategory.getCategoryName());
        return standCategoryResponse;
    }

}
