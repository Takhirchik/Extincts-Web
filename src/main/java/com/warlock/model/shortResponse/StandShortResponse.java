package com.warlock.model.shortResponse;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class StandShortResponse {
    private Long id;
    private String standName;
    private Integer views;
    private UserShortResponse creator;
    private ExtinctShortResponse coverExtinct;
    private Integer extinctsCount;
}
