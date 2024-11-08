package com.jobportal.JobPortal.Service.Entity;

public record AttendanceBanEntity(
        Integer officialAbsenceId,
        String banReason,
        String remarks
) {
}
