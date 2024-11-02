package com.jobportal.JobPortal.Service.DTO;

import java.time.LocalDate;

public record OADateInfoDTO(
        LocalDate officialAbsenceDate,//Dates
        Integer period,//Dates
        String lessonName//lessons
) {
}
