package com.jobportal.JobPortal.Controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record JobSearchOAForm(
        @NotBlank(message = "必須項目です")
        @Pattern(regexp = "briefing | test | visit | other")
        String detail,
        @NotBlank(message = "必須項目です")
        @Size(max = 256, message = "256文字以内で入力してください")
        String companyName,
        @NotBlank(message = "必須項目です")
        @Size(max = 256, message = "256文字以内で入力してください")
        String address
) {
}
