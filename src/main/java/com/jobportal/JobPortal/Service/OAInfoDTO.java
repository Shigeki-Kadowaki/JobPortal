package com.jobportal.JobPortal.Service;

public record OAInfoDTO(
        Integer officialAbsenceId,
        Integer studentId,
        String submissionDate,
        String status,
        String reason,
        Integer periods,
        String lesson_names,
        Integer day_of_weeks
) {
}
