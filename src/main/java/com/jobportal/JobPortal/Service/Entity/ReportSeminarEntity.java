package com.jobportal.JobPortal.Service.Entity;

import com.jobportal.JobPortal.Controller.Form.ReportSeminarForm;
import com.jobportal.JobPortal.Service.EmploymentIntention;
import com.jobportal.JobPortal.Service.NextAction;

public record ReportSeminarEntity(
        Integer reportId,
        String companyName,
        String manager,
        String industry,
        String seminarImpressions,
        EmploymentIntention seminarEmploymentIntention,
        NextAction seminarNextAction
) {
    public ReportSeminarForm toSeminarForm(){
        return new ReportSeminarForm(
            companyName,
            manager,
            industry,
            seminarImpressions,
            String.valueOf(seminarEmploymentIntention),
            String.valueOf(seminarNextAction)
        );
    }
}
