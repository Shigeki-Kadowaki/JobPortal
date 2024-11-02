package com.jobportal.JobPortal.Service.DTO;

import java.time.LocalDate;

public record OAMainInfoDTO(
        Integer officialAbsenceId,
        Integer studentId,
        LocalDate submissionDate,
        String status,
        String reason,
        boolean submittedFlag
) {
}
