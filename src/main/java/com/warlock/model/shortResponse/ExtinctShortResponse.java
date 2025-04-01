package com.warlock.model.shortResponse;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ExtinctShortResponse {
    private Long id;
    private String extinctName;
    private Integer views;
    private Integer likes;
    private UserShortResponse creator;
    private String smallThumbnailUrl;
    private String mediumThumbnailUrl;
    private String largeThumbnailUrl;

}
