package com.warlock.model.request;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateExtinctImageRequest {
    private Long id;
    private String urlImage;
    private CreateExtinctRequest extinct;
}
