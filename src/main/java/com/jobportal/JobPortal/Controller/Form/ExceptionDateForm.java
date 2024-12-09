package com.jobportal.JobPortal.Controller.Form;

import com.jobportal.JobPortal.Service.Entity.ExceptionDateEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
public class ExceptionDateForm {
    @NotBlank(message = "入力してください")
    @Pattern(regexp = "^[0-9]{4}/(0[1-9]|1[0-2])/(0[1-9]|[12][0-9]|3[01])$", message = "yyyy/mm/dd形式で入力してください")
    String exceptionDay;
    @NotNull(message = "選択してください")
    Integer weekdayNumber;

    public ExceptionDateEntity toLocalDate(){
        return  new ExceptionDateEntity(
                LocalDate.parse(exceptionDay, DateTimeFormatter.ofPattern("yyyy/MM/dd")),
                weekdayNumber
        );
    }
}
