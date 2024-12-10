package com.jobportal.JobPortal.Service.Entity;

import java.time.LocalDate;

public record OADatesEntity(
        Integer OAPeriod,
        LocalDate OADate,
        String lessonName
) {
}
