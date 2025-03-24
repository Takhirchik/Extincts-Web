package com.warlock.model.request;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateExtinctRequest {
    private String extinctName;
    private String description;
    private Long userId;
    private Long standId;
}
