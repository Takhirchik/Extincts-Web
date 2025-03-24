package com.warlock.model.request;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateExtinctImageRequest {
    private String urlImage;
    private Long extinctId;
}
