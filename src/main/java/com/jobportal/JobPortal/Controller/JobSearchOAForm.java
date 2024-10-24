package com.jobportal.JobPortal.Controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class JobSearchOAForm{

        private final String name = "jobSearch";

        @NotBlank(message = "必須項目です")
        @Pattern(regexp = "briefing|test|visit|other")
        private String detail;

        @NotBlank(message = "必須項目です")
        private String companyName;

        @NotBlank(message = "必須項目です")
        @Size(max = 256, message = "256文字以内で入力してください")
        private String address;
}
