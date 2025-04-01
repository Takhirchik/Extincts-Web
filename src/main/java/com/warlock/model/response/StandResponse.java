package com.warlock.model.response;

import com.warlock.model.shortResponse.ExtinctShortResponse;
import com.warlock.model.shortResponse.UserShortResponse;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.List;

@Data
@Accessors(chain = true)
public class StandResponse {
    private Long id;
    private String standName;
    private String description;
    private Integer views;
    private LocalDate createdAt;
    private UserShortResponse creator;
    private ExtinctShortResponse coverExtinct;
    private List<ExtinctShortResponse> extincts;
}
