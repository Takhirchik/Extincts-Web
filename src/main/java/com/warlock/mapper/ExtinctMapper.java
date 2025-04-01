package com.warlock.mapper;

import com.warlock.domain.Extinct;
import com.warlock.domain.Stand;
import com.warlock.domain.User;
import com.warlock.model.request.CreateExtinctRequest;
import com.warlock.model.response.ExtinctResponse;
import com.warlock.model.shortResponse.ExtinctShortResponse;
import com.warlock.model.shortResponse.StandShortResponse;
import com.warlock.model.shortResponse.UserShortResponse;

public interface ExtinctMapper {
    Extinct fromRequestToEntity(CreateExtinctRequest request,
                                User creator,
                                Stand stand,
                                String urlImage,
                                String smThumb,
                                String mdThumb,
                                String lgThumb
    );

    ExtinctResponse fromEntityToResponse(
            Extinct extinct,
            UserShortResponse userShortResponse
    );

    ExtinctShortResponse fromEntityToShortResponse(
            Extinct extinct,
            UserShortResponse userShortResponse
    );

}
