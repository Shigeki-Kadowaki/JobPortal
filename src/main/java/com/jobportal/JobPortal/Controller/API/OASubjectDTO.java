package com.jobportal.JobPortal.Controller.API;

import java.time.LocalDate;

public record OASubjectDTO(
        Integer studentId,
                           LocalDate officialAbsenceDate,
        Integer period) {
}
