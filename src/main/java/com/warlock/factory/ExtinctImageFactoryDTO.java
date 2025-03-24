package com.warlock.factory;

import com.warlock.domain.ExtinctImage;
import com.warlock.model.request.CreateExtinctImageRequest;
import com.warlock.model.response.ExtinctImageResponse;
import org.springframework.stereotype.Component;

@Component
public class ExtinctImageFactoryDTO {

    public ExtinctImage fromDTOToEntity(CreateExtinctImageRequest request){
        ExtinctImage extinctImage = new ExtinctImage();
        extinctImage.setUrlImage(request.getUrlImage());
        return extinctImage;
    }

    public ExtinctImageResponse fromEntityToDTO(ExtinctImage image){
        ExtinctImageResponse extinctImageResponse = new ExtinctImageResponse();
        extinctImageResponse.setId(image.getId());
        extinctImageResponse.setUrlImage(image.getUrlImage());
        extinctImageResponse.setExtinctId(image.getExtinct().getId());
        return extinctImageResponse;
    }
}
