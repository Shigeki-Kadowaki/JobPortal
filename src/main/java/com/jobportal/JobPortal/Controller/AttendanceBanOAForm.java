package com.jobportal.JobPortal.Controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AttendanceBanOAForm(
        @NotBlank(message = "必須項目です")
        @Size(max = 256, message = "256文字以内で入力してください")
        String reason
) {
}
