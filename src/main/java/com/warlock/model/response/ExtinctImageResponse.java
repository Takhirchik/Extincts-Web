package com.warlock.model.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ExtinctImageResponse {
    private Long id;
    private String urlImage;
    private Long extinct_id;
}
