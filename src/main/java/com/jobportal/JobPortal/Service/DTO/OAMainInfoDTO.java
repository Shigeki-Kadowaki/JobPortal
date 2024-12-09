package com.jobportal.JobPortal.Service.DTO;

public record OAMainInfoDTO (
        Integer officialAbsenceId,
        Integer studentId,
        boolean reportRequired,
        String status,
        String reason,
        String reportStatus,
        String submittedDate,
        boolean careerCheckRequired,
        boolean teacherCheck,
        Boolean careerCheck,
        Integer version,
        Integer maxVersion,
        String studentEmail
){
}
