package com.jobportal.JobPortal.Service.Entity;

import java.time.LocalDate;
import java.time.LocalTime;

public record OADatesEntity(
        Integer lessonId,
        Integer OAPeriod,
        LocalDate OADate,
        LocalDate submittedDate
) {
}
