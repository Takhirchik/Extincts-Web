package com.warlock.factory;

import com.warlock.domain.Extinct;
import com.warlock.model.request.CreateExtinctRequest;
import com.warlock.model.response.ExtinctResponse;
import org.springframework.stereotype.Component;

@Component
public class ExtinctsFactoryDTO {

    public Extinct fromDTOToEntity (CreateExtinctRequest request){
        Extinct extinct = new Extinct();
        extinct.setExtinctName(request.getExtinctName());
        extinct.setDescription(request.getDescription());
        return extinct;
    }

    public ExtinctResponse fromEntityToDTO(Extinct extinct){
        ExtinctResponse extinctResponse = new ExtinctResponse();
        extinctResponse.setId(extinct.getId());
        extinctResponse.setExtinctName(extinct.getExtinctName());
        extinctResponse.setDescription(extinct.getDescription());
        extinctResponse.setStandName(extinct.getStand().getStandName());
        extinctResponse.setUserNickname(extinct.getUser().getNickname());
        return extinctResponse;
    }
}
