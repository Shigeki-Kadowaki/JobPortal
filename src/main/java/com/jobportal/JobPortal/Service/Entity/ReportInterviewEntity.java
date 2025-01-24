package com.jobportal.JobPortal.Service.Entity;

import com.jobportal.JobPortal.Service.JobInterviewType;

public record ReportInterviewEntity (
        Integer reportId,
        Integer interviewerNumber,
        JobInterviewType interviewType,
        String interviewContent,
        String interviewImpressions
){
}
