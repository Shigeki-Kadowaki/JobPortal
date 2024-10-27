package com.jobportal.JobPortal.Controller;

import com.jobportal.JobPortal.Controller.ValidationGroup.atext;
import com.jobportal.JobPortal.Controller.ValidationGroup.btext;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class validateTestChild {
//    @NotBlank(message = "child text1 error blank", groups = atext.class)
    @NotNull(message = "child text1 error null", groups = atext.class)
    private String text1;
//    @NotBlank(message = "child text2 error blank", groups = btext.class)
    @NotNull(message = "child text2 error null", groups = btext.class)
    private String text2;
}
