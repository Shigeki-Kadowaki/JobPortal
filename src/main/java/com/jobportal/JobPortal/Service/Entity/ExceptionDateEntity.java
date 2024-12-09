package com.jobportal.JobPortal.Service.Entity;

import java.time.LocalDate;


public record ExceptionDateEntity(
        LocalDate exceptionDate,
        Integer weekdayNumber
) {
}
