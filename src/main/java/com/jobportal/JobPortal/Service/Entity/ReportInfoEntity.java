package com.jobportal.JobPortal.Service.Entity;

import com.jobportal.JobPortal.Service.EmploymentIntention;
import com.jobportal.JobPortal.Service.NextAction;
import com.jobportal.JobPortal.Service.OAStatus;
import com.jobportal.JobPortal.Service.ReportType;
import lombok.Data;

@Data
public class ReportInfoEntity {
        Integer officialAbsenceId;
        Integer reportId;
        Integer studentId;
        OAStatus status;
        ReportType reason;
        String submittedDate;
        Integer version;
        Integer maxVersion;
        String studentEmail;
        String companyName;
        Integer activityTime;
        EmploymentIntention employmentIntention;
        NextAction nextAction;
}
