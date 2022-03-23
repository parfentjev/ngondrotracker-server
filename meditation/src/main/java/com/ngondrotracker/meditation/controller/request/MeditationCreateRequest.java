package com.ngondrotracker.meditation.controller.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class MeditationCreateRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String path;

    @NotNull
    private Integer goal;

    @NotNull
    private Integer order;
}
