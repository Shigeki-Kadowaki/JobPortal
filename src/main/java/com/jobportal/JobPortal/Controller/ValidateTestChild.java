package com.jobportal.JobPortal.Controller;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import constants.VG.a;
import constants.VG.b;

@Data
public class ValidateTestChild {
    @NotBlank(message = "child1 error",groups = a.class)
    private String text;
    @NotBlank(message = "child2 error",groups = b.class)
    private  String text2;
}
