package com.jobportal.JobPortal.Service.DTO;

import com.jobportal.JobPortal.Service.OAReason;

import java.time.LocalDate;

public record OAMainInfoDTO (
        Integer officialAbsenceId,
        Integer studentId,
        boolean reportRequired,
        String status,
        String reason,
        String reportStatus,
        String submittedDate
){
}
