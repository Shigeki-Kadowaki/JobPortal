package com.jobportal.JobPortal.Service;

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
    Long studentId;
    LocalDate submissionDate;
    boolean jobSearchFlag;
    boolean teacherCheck;
    Boolean careerCheck;
    OAStatus status;
    OAReason reason;

    public OAMainEntity(Integer officialAbsenceId,
                        Long studentId,
                        LocalDate submissionDate,
                        Boolean jobSearchFlag,
                        Boolean teacherCheck,
                        Boolean careerCheck,
                        OAStatus status,
                        OAReason reason) {
        this.officialAbsenceId = officialAbsenceId;
        this.studentId = studentId;
        this.submissionDate = submissionDate;
        this.jobSearchFlag = jobSearchFlag;
        this.teacherCheck = teacherCheck;
        this.careerCheck = careerCheck;
        this.status = status;
        this.reason = reason;
    }
}
