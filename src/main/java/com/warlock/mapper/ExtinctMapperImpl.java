package com.warlock.mapper;

import com.warlock.domain.Extinct;
import com.warlock.domain.Stand;
import com.warlock.domain.User;
import com.warlock.model.request.CreateExtinctRequest;
import com.warlock.model.request.UpdateExtinctRequest;
import com.warlock.model.response.ExtinctResponse;
import com.warlock.model.shortResponse.ExtinctShortResponse;
import com.warlock.model.shortResponse.StandShortResponse;
import com.warlock.model.shortResponse.UserShortResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ExtinctMapperImpl implements ExtinctMapper {
    @Override
    public Extinct fromCreateRequestToEntity(CreateExtinctRequest request,
                                           User creator,
                                           Stand stand,
                                           String urlImage,
                                           String smThumb,
                                           String mdThumb,
                                           String lgThumb
    ){
        Extinct extinct = new Extinct();
        extinct.setExtinctName(request.getExtinctName());
        extinct.setDescription(request.getDescription());
        extinct.setUrlImage(urlImage);
        extinct.setSmallThumbnailUrl(smThumb);
        extinct.setMediumThumbnailUrl(mdThumb);
        extinct.setLargeThumbnailUrl(lgThumb);
        extinct.setStand(stand);
        extinct.setCreator(creator);
        return extinct;
    }

    @Override
    public Extinct fromUpdateRequestToEntity(UpdateExtinctRequest request, User creator, Stand stand){
        Extinct extinct = new Extinct();
        extinct.setExtinctName(request.getExtinctName());
        extinct.setDescription(request.getDescription());
        extinct.setStand(stand);
        extinct.setCreator(creator);
        return extinct;
    }

    @Override
    public ExtinctResponse fromEntityToResponse(
            Extinct extinct,
            UserShortResponse userShortResponse
    ){
        ExtinctResponse response = new ExtinctResponse();
        response.setId(extinct.getId());
        response.setExtinctName(extinct.getExtinctName());
        response.setDescription(extinct.getDescription());
        response.setCreator(userShortResponse);
        response.setLikes(extinct.getLikes());
        response.setViews(extinct.getViews());
        response.setCreatedAt(extinct.getCreatedAt());
        response.setUrl_img(extinct.getUrlImage());
        return response;
    }

    @Override
    public ExtinctShortResponse fromEntityToShortResponse(
            Extinct extinct,
            UserShortResponse userShortResponse
    ){
        ExtinctShortResponse response = new ExtinctShortResponse();
        response.setId(extinct.getId());
        response.setExtinctName(extinct.getExtinctName());
        response.setLikes(extinct.getLikes());
        response.setViews(extinct.getViews());
        response.setLargeThumbnailUrl(extinct.getLargeThumbnailUrl());
        response.setMediumThumbnailUrl(extinct.getMediumThumbnailUrl());
        response.setSmallThumbnailUrl(extinct.getSmallThumbnailUrl());
        response.setCreator(userShortResponse);
        return response;
    }

}
