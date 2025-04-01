package com.warlock.model.response;

import com.warlock.model.shortResponse.StandShortResponse;
import com.warlock.model.shortResponse.UserShortResponse;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Data
@Accessors(chain = true)
public class ExtinctResponse {
    private Long id;
    private String extinctName;
    private String description;
    private Integer views;
    private Integer likes;
    private LocalDate createdAt;
    private UserShortResponse creator;
    private String url_img;
}
