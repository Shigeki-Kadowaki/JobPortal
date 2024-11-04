package com.jobportal.JobPortal.Service.Entity;

import java.time.LocalDate;

public record OADateInfoEntity(
        LocalDate officialAbsenceDate,//Dates
        Integer period,//Dates
        String lessonName//lessons
) {
}
