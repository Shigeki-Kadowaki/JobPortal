package com.jobportal.JobPortal.Controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BereavementOAForm(
        @NotBlank(message = "必須項目です")
        @Size(max = 32, message = "32文字以内で入力してください")
        String deceasedName,
        @NotBlank(message = "必須項目です")
        @Size(max = 8, message = "8文字以内で入力してください")
        String relationship
) {
}
