package com.warlock.model.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class StandResponse {
    private Long id;
    private String standName;
    private String description;
    private String userNickname;
    private StandCategoryResponse standCategory;
}
