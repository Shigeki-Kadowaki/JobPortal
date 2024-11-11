package com.jobportal.JobPortal.Service.Entity;

public record SeminarEntity (
        Integer officialAbsenceId,
        String seminarName,
        String location,
        String venueName,
        String remarks,
        Integer visitStartHour,
        Integer visitStartMinute
){
}
