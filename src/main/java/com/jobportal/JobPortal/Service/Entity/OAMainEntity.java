package com.jobportal.JobPortal.Service.Entity;

import com.jobportal.JobPortal.Service.OAReason;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@RequiredArgsConstructor
public class OAMainEntity {
    final Integer officialAbsenceId;
    final Integer studentId;
    final Boolean reportRequired;
    final String status;
    final OAReason reason;
    final boolean reportSubmittedFlag;
    final boolean careerCheckRequired;
    final boolean teacherCheck;
    final Boolean careerCheck;
    final Integer grade;
    final String classroom;
    final String course;
    final String studentName;
    final String studentEmail;
}
