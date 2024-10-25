package com.jobportal.JobPortal.Controller;

import com.jobportal.JobPortal.Controller.ValidationGroup.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class validateTest {
    @NotBlank
    @NotNull
    private String defaultText;
    @NotBlank(message = "text1blank", groups = {atext.class})
    @NotNull(message = "text1null", groups = {atext.class})
    private String text1;
    @NotBlank(message = "text2blank", groups = {btext.class})
    @NotNull(message = "text2null", groups = {btext.class})
    private String text2;
}
