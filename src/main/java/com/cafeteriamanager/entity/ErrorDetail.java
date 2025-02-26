package com.cafeteriamanager.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ErrorDetail {
    @Schema(description = "Brief error message",example = "NOT_FOUND")
    private  String message;
    @Schema(description = "Cause for the error", example = "not found for id")
    private String details;
}
