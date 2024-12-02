package com.jobportal.JobPortal.Service.Entity;

public record TimeTableEntity(
        Integer weekdayNumber,
        Integer period,
        Integer subjectId
) {
}
