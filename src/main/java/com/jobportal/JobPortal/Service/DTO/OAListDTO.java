package com.jobportal.JobPortal.Service.DTO;

import com.jobportal.JobPortal.Service.OAReason;
import com.jobportal.JobPortal.Service.OAStatus;

import java.time.LocalDate;

public record OAListDTO(
        Integer officialAbsenceId,
        Integer studentId,
        String status,
        String reason,
        boolean submittedFlag,
        String reportStatus,
        String startDate,
        String endDate
){


}
