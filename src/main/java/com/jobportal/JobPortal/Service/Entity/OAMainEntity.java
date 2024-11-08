package com.jobportal.JobPortal.Service.Entity;

import com.jobportal.JobPortal.Service.OAReason;
import com.jobportal.JobPortal.Service.OAStatus;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
public class OAMainEntity
{
    final Integer officialAbsenceId;
    final Integer studentId;
    final Boolean reportRequired;
    final String status;
    final String reason;
    final boolean reportSubmittedFlag;
    final LocalDate submittedDate;
}
