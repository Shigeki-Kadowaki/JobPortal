package com.jobportal.JobPortal.Service.Entity;

import com.jobportal.JobPortal.Service.CompanyDecision;
import com.jobportal.JobPortal.Service.NextAction;
import com.jobportal.JobPortal.Service.OAStatus;
import com.jobportal.JobPortal.Service.ReportType;

import java.time.LocalDate;

public record ReportInfoEntity(
        Integer officialAbsenceId,
        Integer reportId,
        Integer studentId,
        OAStatus status,
        ReportType reason,
        LocalDate submittedDate,
        Integer version,
        Integer maxVersion,
        String studentEmail,
        Integer activityTime,
        CompanyDecision companyDecision,
        NextAction nextAction
) {
}
