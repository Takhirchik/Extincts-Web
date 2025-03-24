package com.warlock.factory;

import com.warlock.domain.Stand;
import com.warlock.model.request.CreateStandRequest;
import com.warlock.model.response.StandCategoryResponse;
import com.warlock.model.response.StandResponse;
import com.warlock.repository.StandRepository;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class StandsFactoryDTO {

    public Stand fromDTOToEntity(CreateStandRequest request){
        Stand stand = new Stand();
        stand.setStandName(request.getStandName());
        stand.setDescription(request.getDescription());
        return stand;
    }

    public StandResponse fromEntityToDTO(Stand stand, StandCategoryResponse standCategoryResponse){
        StandResponse standResponse = new StandResponse();
        standResponse.setStandName(stand.getStandName());
        standResponse.setDescription(stand.getDescription());
        standResponse.setUserNickname(stand.getUser().getNickname());
        standResponse.setStandCategory(standCategoryResponse);
        return standResponse;
    }
}
