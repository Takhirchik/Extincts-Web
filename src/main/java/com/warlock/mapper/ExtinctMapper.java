package com.warlock.mapper;

import com.warlock.domain.Extinct;
import com.warlock.domain.Stand;
import com.warlock.domain.User;
import com.warlock.model.request.CreateExtinctRequest;
import com.warlock.model.request.UpdateExtinctRequest;
import com.warlock.model.response.ExtinctResponse;
import com.warlock.model.shortResponse.ExtinctShortResponse;
import com.warlock.model.shortResponse.UserShortResponse;

public interface ExtinctMapper {
    Extinct fromCreateRequestToEntity(CreateExtinctRequest request,
                                User creator,
                                Stand stand,
                                String urlImage,
                                String smThumb,
                                String mdThumb,
                                String lgThumb
    );

    Extinct fromUpdateRequestToEntity(UpdateExtinctRequest request, User creator, Stand stand);

    ExtinctResponse fromEntityToResponse(
            Extinct extinct,
            UserShortResponse userShortResponse
    );

    ExtinctShortResponse fromEntityToShortResponse(
            Extinct extinct,
            UserShortResponse userShortResponse
    );

}
