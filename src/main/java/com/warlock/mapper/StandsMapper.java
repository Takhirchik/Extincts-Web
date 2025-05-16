package com.warlock.mapper;

import com.warlock.domain.Stand;
import com.warlock.domain.User;
import com.warlock.model.request.CreateStandRequest;
import com.warlock.model.response.StandResponse;
import com.warlock.model.shortResponse.ExtinctShortResponse;
import com.warlock.model.shortResponse.StandShortResponse;
import com.warlock.model.shortResponse.UserShortResponse;

import java.util.List;

public interface StandsMapper {
    Stand fromRequestToEntity(CreateStandRequest request, User user);
    StandResponse fromEntityToResponse(
            Stand stand,
            UserShortResponse userShortResponse,
            ExtinctShortResponse coverImage,
            List<ExtinctShortResponse> extincts
            );

    StandShortResponse fromEntityToShortResponse(
            Stand stand,
            UserShortResponse userShortResponse,
            ExtinctShortResponse coverImage
    );
}
