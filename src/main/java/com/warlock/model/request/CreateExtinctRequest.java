package com.warlock.model.request;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
@Accessors(chain = true)
public class CreateExtinctRequest {
    private String extinctName;
    private String description;
    private MultipartFile image;
    private Long standId;
}
