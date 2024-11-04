package com.jobportal.JobPortal.Service.DTO;

public record OAMainInfoDTO (
        Integer officialAbsenceId,
        Integer studentId,
        String submissionDate,
        String status,
        String reason
){
}
