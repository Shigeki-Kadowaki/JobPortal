package com.jobportal.JobPortal.Controller.Form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SeminarOAForm {

    private final String name = "seminar";

    @NotBlank(message = "必須項目です")
    private String seminarName;
    @NotBlank(message = "必須項目です")
    private String location;
    @NotBlank(message = "必須項目です")
    private String venueName;
}
