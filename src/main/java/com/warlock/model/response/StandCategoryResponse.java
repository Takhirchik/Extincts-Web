package com.warlock.model.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class StandCategoryResponse {
    private Long id;
    private String categoryName;

}
