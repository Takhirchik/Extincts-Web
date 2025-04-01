package com.warlock.model.request;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateStandRequest {
    private String standName;
    private String description;
}
