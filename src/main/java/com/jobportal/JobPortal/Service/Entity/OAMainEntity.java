package com.jobportal.JobPortal.Service.Entity;

import com.jobportal.JobPortal.Service.OAReason;
import com.jobportal.JobPortal.Service.OAStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
public class OAMainEntity
{
    final Integer officialAbsenceId;
    final Integer studentId;
    final boolean reportRequired;
    final OAStatus status;
    final OAReason reason;
    final boolean reportSubmittedFlag;
}
