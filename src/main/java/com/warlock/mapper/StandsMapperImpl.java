package com.warlock.mapper;

import com.warlock.domain.Stand;
import com.warlock.domain.User;
import com.warlock.model.request.CreateStandRequest;
import com.warlock.model.response.StandResponse;
import com.warlock.model.shortResponse.ExtinctShortResponse;
import com.warlock.model.shortResponse.StandShortResponse;
import com.warlock.model.shortResponse.UserShortResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StandsMapperImpl implements StandsMapper{

    @Override
    public Stand fromRequestToEntity(@NotNull CreateStandRequest request, User user){
        Stand stand = new Stand();
        stand.setStandName(request.getStandName());
        stand.setDescription(request.getDescription());
        stand.setCreator(user);
        return stand;
    }

    @Override
    public StandResponse fromEntityToResponse(
            Stand stand,
            UserShortResponse userShortResponse,
            ExtinctShortResponse coverImage,
            List<ExtinctShortResponse> extincts
    ){
        StandResponse response = new StandResponse();
        response.setId(stand.getId());
        response.setStandName(stand.getStandName());
        response.setDescription(stand.getDescription());
        response.setCreatedAt(stand.getCreatedAt());
        response.setViews(stand.getViews());
        response.setExtincts(extincts);
        response.setCoverExtinct(coverImage);
        response.setCreator(userShortResponse);
        return response;
    }

    @Override
    public StandShortResponse fromEntityToShortResponse(
            Stand stand,
            UserShortResponse userShortResponse,
            ExtinctShortResponse coverImage
    ){
        StandShortResponse response = new StandShortResponse();
        response.setId(stand.getId());
        response.setStandName(stand.getStandName());
        response.setCoverExtinct(coverImage);
        response.setViews(stand.getViews());
        response.setCreator(userShortResponse);
        response.setExtinctsCount(List.of(stand.getExtincts()).size());
        return response;
    }
}
