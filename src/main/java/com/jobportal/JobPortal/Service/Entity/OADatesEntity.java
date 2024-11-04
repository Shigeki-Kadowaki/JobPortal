package com.jobportal.JobPortal.Service.Entity;

import java.time.LocalDate;

public record OADatesEntity(
        Integer lessonId,
        Integer OAPeriod,
        LocalDate OADate
) {
}
