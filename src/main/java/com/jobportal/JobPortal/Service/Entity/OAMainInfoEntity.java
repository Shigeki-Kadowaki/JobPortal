package com.jobportal.JobPortal.Service.Entity;

import com.jobportal.JobPortal.Service.DTO.OAMainInfoDTO;
import com.jobportal.JobPortal.Service.MainService;
import com.jobportal.JobPortal.Service.OAReason;
import com.jobportal.JobPortal.Service.OAStatus;

import java.time.LocalDate;

public record OAMainInfoEntity(
        Integer officialAbsenceId,
        Integer studentId,
        boolean reportRequired,
        OAStatus OAStatus,
        OAReason reason,
        OAStatus reportStatus,
        LocalDate submittedDate,
        boolean careerCheckRequired,
        boolean teacherCheck,
        Boolean careerCheck,
        Integer version,
        Integer maxVersion,
        String studentEmail
) {
    public OAMainInfoDTO toInfoDTO() {
        return new OAMainInfoDTO(
            officialAbsenceId,
            studentId,
            reportRequired,
            OAStatus.getJapaneseName(),
            reason.getJapaneseName(),
            MainService.existsReport(reportStatus),
            MainService.dateFormat(submittedDate),
            careerCheckRequired,
            teacherCheck,
            careerCheck,
            version,
            maxVersion,
            studentEmail
        );
    }


}
