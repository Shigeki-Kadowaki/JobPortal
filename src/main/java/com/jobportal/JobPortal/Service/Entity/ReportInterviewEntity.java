package com.jobportal.JobPortal.Service.Entity;

public record ReportInterviewEntity (
        Integer reportId,
        Integer interviewerNumber,
        String interviewType,
        String interviewContent,
        String interviewImpressions
){
}
