package com.jobportal.JobPortal.Controller;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Form
{

    @NotBlank
    private String text;

}
