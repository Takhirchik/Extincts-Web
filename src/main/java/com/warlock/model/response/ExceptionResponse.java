package com.warlock.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.Instant;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
public class ExceptionResponse {
    private String message;
    private Instant timestamp;
}