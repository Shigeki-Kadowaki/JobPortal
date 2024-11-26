package com.jobportal.JobPortal.Controller.Form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ClassificationForm {
    @NotNull(message = "入力してください")
    Integer grade;
    @NotEmpty(message = "入力してください")
    String classroom;
    @NotEmpty(message = "入力してください")
    String course;
    @NotEmpty(message = "入力してください")
    String semester;
}
