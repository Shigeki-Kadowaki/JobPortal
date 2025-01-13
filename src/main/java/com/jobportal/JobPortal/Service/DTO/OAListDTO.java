package com.jobportal.JobPortal.Service.DTO;

import java.util.List;

public record OAListDTO(
        Integer officialAbsenceId,
        Integer studentId,
        Integer grade,
        String classroom,
        String course,
        String name,
        String status,
        String reason,
        String reportStatus,
        boolean reportRequired,
        String startDate,
        String endDate,
        boolean reportSubmittedFlag,
        String jobSearchVisitStartHour,
        String jobSearchVisitStartMinute,
        String seminarVisitStartHour,
        String seminarVisitStartMinute,
        List<Integer> lessons
){


}
