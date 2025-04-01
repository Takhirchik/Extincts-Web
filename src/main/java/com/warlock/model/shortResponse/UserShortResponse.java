package com.warlock.model.shortResponse;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserShortResponse {
    private Long id;
    private String nickname;
    private String smallThumbnailUrl;
    private String mediumThumbnailUrl;
    private String largeThumbnailUrl;
}
