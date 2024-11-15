package com.jobportal.JobPortal.Service.Entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@RequiredArgsConstructor
public class OAMainEntity {
    final Integer officialAbsenceId;
    final Integer studentId;
    final Boolean reportRequired;
    final String status;
    final String reason;
    final boolean reportSubmittedFlag;
    final boolean careerCheckRequired;
    final boolean teacherCheck;
    final Boolean careerCheck;
}
