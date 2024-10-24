package com.jobportal.JobPortal.Controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BereavementOAForm {

         private final String name = "bereavement";

        @NotBlank(message = "必須項目です")
        @Size(max = 64, message = "64文字以内で入力してください")
         private String deceasedName;
        @NotBlank(message = "必須項目です")
        @Size(max = 64, message = "64文字以内で入力してください")
         private String relationship;
}
