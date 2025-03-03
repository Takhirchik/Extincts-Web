package com.warlock.model.request;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateStandRequest {
    private Long id;
    private String standName;
    private String description;
    private CreateStandCategoryRequest standCategory;

}
