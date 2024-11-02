package com.jobportal.JobPortal.Service.Entity;

import java.time.LocalDate;

public record OADatesEntity(
        LocalDate OADate,
        Integer OAPeriod
) {
}
