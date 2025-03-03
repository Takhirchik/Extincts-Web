package com.warlock.model.request;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateStandCategoryRequest {
    private Long id;
    private String categoryName;

}
