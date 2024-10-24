package com.jobportal.JobPortal.Controller;

import constants.VG.a;
import constants.VG.b;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class validateTest {
    private String radio;

    @NotNull(message = "parent error",groups = a.class)
    @Valid
    private ValidateTestChild test;

    @NotNull(message = "parent error",groups = b.class)
    @Valid
    private ValidateTestChild test2;
}
