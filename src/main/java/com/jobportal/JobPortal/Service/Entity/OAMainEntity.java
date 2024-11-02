package com.jobportal.JobPortal.Service.Entity;

import com.jobportal.JobPortal.Service.OAReason;
import com.jobportal.JobPortal.Service.OAStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class OAMainEntity


//        JobSearchEntity jobSearchEntity
//        BereavementOAForm bereaveForm,
//        AttendanceBanOAForm banForm,
//        OtherOAForm otherForm
{
    Integer officialAbsenceId;
    Integer studentId;
    LocalDate submissionDate;
    boolean jobSearchFlag;
    boolean teacherCheck;
    boolean submittedFlag;
    Boolean careerCheck;
    String status;
    OAReason reason;

    public OAMainEntity(
            Integer officialAbsenceId,
            Integer studentId,
            LocalDate submissionDate,
            Boolean jobSearchFlag,
            Boolean teacherCheck,
            Boolean careerCheck,
            Boolean submittedFlag,
            String status,
            OAReason reason) {
        this.officialAbsenceId = officialAbsenceId;
        this.studentId = studentId;
        this.submissionDate = submissionDate;
        this.jobSearchFlag = jobSearchFlag;
        this.teacherCheck = teacherCheck;
        this.careerCheck = careerCheck;
        this.submittedFlag = submittedFlag;
        this.status = status;
        this.reason = reason;
    }
}
