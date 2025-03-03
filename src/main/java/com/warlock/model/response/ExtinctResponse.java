package com.warlock.model.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ExtinctResponse {
    private Long id;
    private String extinctName;
    private String description;
    private String standName;
}
