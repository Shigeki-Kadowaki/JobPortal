package com.jobportal.JobPortal.Controller.Form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ExceptionDate {
    @NotBlank(message = "入力してください")
    @Pattern(regexp = "^[0-9]{4}/(0[1-9]|1[0-2])/(0[1-9]|[12][0-9]|3[01])$", message = "yyyy/mm/dd形式で入力してください")
    String exceptionDay;
    @NotNull(message = "選択してください")
    Integer weekdayNumber;
}
